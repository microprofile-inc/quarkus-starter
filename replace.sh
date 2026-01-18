#!/usr/bin/env bash
set -euo pipefail

#预览： ./replace.sh "旧字符串" "新字符串" . --ext=java,xml --dry-run
#执行替换并备份： ./replace.sh "旧" "新" . --ext=java,xml --backup --yes

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 OLD NEW [DIR] [--ext=ext1,ext2] [--dry-run] [--backup] [--yes]"
  exit 1
fi

OLD="$1"; NEW="$2"; shift 2
DIR="."
EXTS=""
DRY_RUN=false
BACKUP=false
YES=false

# parse remaining args
while [[ $# -gt 0 ]]; do
  case "$1" in
    --ext=*) EXTS="${1#*=}"; shift ;;
    --dry-run) DRY_RUN=true; shift ;;
    --backup) BACKUP=true; shift ;;
    --yes) YES=true; shift ;;
    *) DIR="$1"; shift ;;
  esac
done

if [[ ! -d "$DIR" ]]; then
  echo "Directory $DIR not found"
  exit 2
fi

# prepare exclude patterns
EXCLUDE='*/.git/* */build/* */target/* */.gradle/* */out/*'
# build find command and collect files
mapfile -d '' FILES < <(find "$DIR" \( -path '*/.git' -o -path '*/build' -o -path '*/target' -o -path '*/.gradle' -o -path '*/out' \) -prune -o -type f -print0)

# filter by extensions if provided
if [[ -n "$EXTS" ]]; then
  IFS=',' read -r -a ARR <<< "$EXTS"
  FILTERED=()
  for f in "${FILES[@]}"; do
    for e in "${ARR[@]}"; do
      if [[ "${f,,}" == *".${e,,}" ]]; then
        FILTERED+=("$f")
        break
      fi
    done
  done
  FILES=("${FILTERED[@]}")
fi

# find files that contain OLD (binary files ignored)
MATCHES=()
for f in "${FILES[@]}"; do
  if grep -I -qF -- "$OLD" "$f" 2>/dev/null; then
    MATCHES+=("$f")
  fi
done

if [[ ${#MATCHES[@]} -eq 0 ]]; then
  echo "No matches for '$OLD'"
  exit 0
fi

echo "Found ${#MATCHES[@]} file(s) containing the target."

if $DRY_RUN; then
  echo "---- DRY RUN: showing matches ----"
  for f in "${MATCHES[@]}"; do
    echo "File: $f"
    grep -n --color=never -F -- "$OLD" "$f" || true
  done
  exit 0
fi

if ! $YES; then
  read -r -p "Proceed to replace '$OLD' => '$NEW' in ${#MATCHES[@]} file(s)? [y/N] " ans
  case "$ans" in [yY]|[yY][eE][sS]) ;; *) echo "Aborted."; exit 1 ;; esac
fi

# export for perl to access
export OLD NEW

COUNT=0
for f in "${MATCHES[@]}"; do
  if $BACKUP; then
    perl -0777 -i.bak -pe 'BEGIN{$r=$ENV{"NEW"}; $r =~ s/\\/\\\\/g;} s/\Q$ENV{"OLD"}\E/$r/g' -- "$f"
  else
    perl -0777 -i -pe 'BEGIN{$r=$ENV{"NEW"}; $r =~ s/\\/\\\\/g;} s/\Q$ENV{"OLD"}\E/$r/g' -- "$f"
  fi
  ((COUNT++))
done

echo "Replaced in $COUNT file(s)."
if $BACKUP; then
  echo "Backups created with \".bak\" suffix."
fi
