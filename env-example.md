# 환경변수 설정 예시 (.env 파일)

## Database Configuration
```bash
MYSQL_ROOT_PASSWORD=your_mysql_root_password
MYSQL_USER=community_user
MYSQL_PASSWORD=your_mysql_password
```

## JWT Configuration
```bash
JWT_SECRET=your_jwt_secret_key_here_minimum_256_bits
JWT_ACCESS_MS=3600000
JWT_REFRESH_MS=1209600000
```

## File Upload Configuration
```bash
UPLOAD_DIR=/app/uploads
PUBLIC_BASE_URL=http://localhost:8080/uploads
```

## Default Avatar URL
```bash
DEFAULT_AVATAR_URL=https://community-image-bucket-1116.s3.ap-northeast-2.amazonaws.com/avatar-default.png
```

## Cookie Configuration
```bash
COOKIE_DOMAIN=localhost
COOKIE_SECURE=false
COOKIE_SAMESITE=Lax
```

## Application Configuration
```bash
SWAGGER_ENABLED=true
THYMELEAF_CACHE=false
```

## Logging Configuration
```bash
JPA_SHOW_SQL=true
LOG_SQL_LEVEL=debug
LOG_SQL_BIND_LEVEL=trace
LOG_SPRING_JPA_LEVEL=debug
LOG_HBM2DDL_LEVEL=debug
LOG_JDBC_BIND_LEVEL=trace
LOG_SECURITY_LEVEL=DEBUG
```

## Multipart Configuration
```bash
MULTIPART_MAX_FILE_SIZE=50MB
MULTIPART_MAX_REQUEST_SIZE=50MB
```
