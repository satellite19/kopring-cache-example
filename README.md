# Kopring Cache Example

Spring Boot 3.x + Kotlin + Caffeine Cache를 사용한 캐시 관리 시스템입니다.

## 주요 기능

1. **Todo CRUD API** - 기본적인 할 일 관리 기능
2. **가맹점 정보 조회** - 복합키(merchantId + storeId)로 캐시 관리
3. **오류 코드 조회** - 카드/가상계좌 오류 코드 캐싱
4. **캐시 관리 API** - 캐시 초기화 및 재적재
5. **자동 캐시 동기화** - 4시간마다 자동 실행

## 프로젝트 구조

```
src/main/kotlin/com/example/kopringcacheexample/
├── application/
│   ├── dto/
│   │   ├── TodoDto.kt
│   │   ├── MerchantDto.kt
│   │   └── ErrorCodeDto.kt
│   └── service/
│       ├── TodoApplicationService.kt
│       └── CacheManagementService.kt
├── domain/
│   ├── model/
│   │   ├── Todo.kt
│   │   ├── Merchant.kt
│   │   ├── ErrorCode.kt
│   │   ├── CardErrorCode.kt
│   │   └── VirtualAccountErrorCode.kt
│   ├── repository/
│   │   ├── TodoRepository.kt
│   │   ├── MerchantRepository.kt
│   │   ├── CardErrorCodeRepository.kt
│   │   └── VirtualAccountErrorCodeRepository.kt
│   └── service/
│       ├── TodoDomainService.kt
│       ├── MerchantDomainService.kt
│       └── ErrorCodeDomainService.kt
└── infrastructure/
    ├── adapter/
    │   ├── controller/
    │   │   ├── TodoController.kt
    │   │   ├── MerchantController.kt
    │   │   ├── ErrorCodeController.kt
    │   │   └── CacheReloadController.kt
    │   └── filter/
    │       └── CacheReloadAuthFilter.kt
    └── configuration/
        ├── CacheNames.kt
        ├── CacheConfig.kt
        ├── CacheInitializer.kt
        ├── CacheScheduler.kt
        ├── DataInitializer.kt
        └── WebConfig.kt
```

## API 엔드포인트

### Todo API
- GET `/api/todos` - 모든 할 일 조회
- GET `/api/todos/{id}` - 특정 할 일 조회
- POST `/api/todos` - 할 일 생성
- PUT `/api/todos/{id}` - 할 일 수정
- DELETE `/api/todos/{id}` - 할 일 삭제

### 가맹점 API
- GET `/api/merchants` - 가맹점 정보 조회
```json
{
  "merchantId": "M001",
  "storeId": "S001"
}
```

### 오류 코드 API
- GET `/api/error-codes/card/{errorCode}` - 카드 오류 코드 조회
- GET `/api/error-codes/virtual-account/{errorCode}` - 가상계좌 오류 코드 조회

### 캐시 재적재 API (인증 필요)
- POST `/api/cache-reload/all` - 모든 캐시 재적재
- POST `/api/cache-reload/{cacheName}` - 특정 캐시 재적재

가능한 캐시 이름:
- `todo`
- `merchant`
- `card_error_code`
- `virtual_account_error_code`

## 인증 설정

캐시 재적재 API는 Basic Auth와 IP 필터링이 적용됩니다.

### application.properties 설정
```properties
# 허용된 IP 목록 (쉼표로 구분)
cache.reload.allowed-ips=127.0.0.1,0:0:0:0:0:0:0:1
# Basic Auth 계정
```

### 인증 방법
```bash
# 모든 캐시 재적재
curl -X POST http://localhost:8080/api/cache-reload/all \
     -u id/pwd

# 특정 캐시 재적재
curl -X POST http://localhost:8080/api/cache-reload/merchant \
     -u id/pwd
```

## H2 데이터베이스 콘솔

개발 시 H2 콘솔에 접근할 수 있습니다:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: (비워둠)

## 캐시 설정

- maximumSize: 1000 (최대 캐시 항목 수)
- LRU 방식으로 오래된 항목 제거
- expireAfterWrite는 설정하지 않음
- 4시간마다 자동으로 전체 캐시 동기화

## 테스트 데이터

애플리케이션 시작 시 다음 테스트 데이터가 자동으로 생성됩니다:

### 가맹점 데이터
- M001_S001 - 테스트 가맹점1 (ACTIVE)
- M001_S002 - 테스트 가맹점2 (ACTIVE)
- M002_S001 - 테스트 가맹점3 (INACTIVE)

### 카드 오류 코드
- CE001 - 카드 인증 실패
- CE002 - 잔액 부족
- CE003 - 카드 만료
- CE004 - 분실 카드
- CE005 - 한도 초과

### 가상계좌 오류 코드
- VA001 - 계좌번호 오류
- VA002 - 은행 점검 중
- VA003 - 입금 한도 초과
- VA004 - 일일 한도 초과
- VA005 - 시스템 오류
