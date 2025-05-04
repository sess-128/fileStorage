#!/bin/sh

set -e

echo "⏳ Ждём MinIO..."
until mc alias set myminio http://minioS3:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD" 2>/dev/null; do
  echo "⏳ MinIO ещё не готов, ждём..."
  sleep 3
done

echo "✅ MinIO доступен. Настраиваем..."

mc admin user add myminio "$MINIO_APP_USER" "$MINIO_APP_PASSWORD" || echo "👤 Пользователь уже существует"
mc admin policy attach myminio readwrite --user="$MINIO_APP_USER"
mc mb -p myminio/user-files || echo "🪣 Бакет уже существует"
