#!/bin/bash
# setup-pre-commit.sh

HOOK_SRC="pre-commit/pre-commit"
HOOK_DEST=".git/hooks/pre-commit"

if [ ! -f "$HOOK_SRC" ]; then
  echo "Source hook $HOOK_SRC not found."
  exit 1
fi

cp "$HOOK_SRC" "$HOOK_DEST"
chmod +x "$HOOK_DEST"
echo "Pre-commit hook installed."