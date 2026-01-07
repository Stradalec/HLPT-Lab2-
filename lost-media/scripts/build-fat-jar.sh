#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="${SCRIPT_DIR}/.."
TEMP_DIR="${PROJECT_ROOT}/build/fat"

cd "$PROJECT_ROOT"

echo "[fat-jar] Собираем обычный app.jar через compile.sh"
bash "${SCRIPT_DIR}/compile.sh"

echo "[fat-jar] Готовим временную директорию"
rm -rf "$TEMP_DIR"
mkdir -p "$TEMP_DIR"

echo "[fat-jar] Распаковываем app.jar"
cd "$TEMP_DIR"
jar xf ../../app.jar

echo "[fat-jar] Распаковываем все зависимости из libs/"
for dep in ../../libs/*.jar; do
  echo "  -> добавляем $(basename "$dep")"
  jar xf "$dep" || true
done

echo "[fat-jar] Создаём MANIFEST.MF c Main-Class: AppKt"
cat > MANIFEST.MF <<EOF
Manifest-Version: 1.0
Main-Class: AppKt

EOF

echo "[fat-jar] Собираем app-fat.jar в корне проекта"
jar cfm ../../app-fat.jar MANIFEST.MF .

echo "[fat-jar] Очищаем временные файлы"
cd "$PROJECT_ROOT"
rm -rf "$TEMP_DIR"

echo "[fat-jar] Готово: app-fat.jar"
