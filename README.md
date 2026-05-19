# chat-service
채팅 생명주기를 담당하는 Spring Boot 기반 마이크로서비스입니다.
거래 생성 이벤트를 수신해 채팅방을 자동 생성하고, WebSocket STOMP 기반 실시간 채팅 기능을 제공합니다.

## 주요 기능
- 거래 생성 Kafka 이벤트 수신 후 채팅방 자동 생성
- 채팅방 목록/단건 조회 REST API 제공
- WebSocket STOMP 기반 실시간 메시지 송수신
- 거래 완료/취소 이벤트 수신 후 채팅방 상태 변경
- Outbox 패턴을 통한 채팅방 생성/메시지 전송 이벤트 발행

## 기술 스택
- Java 21
- Spring Boot 3.5
- Spring Cloud 2025.0.2
- Spring Data JPA
- QueryDSL 6.8
- Spring Kafka
- WebSocket STOMP
- Eureka Client
- PostgreSQL
- Docker, Docker Compose

## 프로젝트 구조

```text
src/main/java/org/pgsg/chat
├── application      # 유스케이스, 서비스, 쿼리 서비스, 커맨드/결과 DTO
├── domain           # 채팅 도메인 모델, 값 객체, 도메인 예외
├── infrastructure   # JPA 레포지토리 구현체, Kafka, WebSocket 인프라 어댑터
└── presentiation    # REST 컨트롤러, WebSocket 핸들러, API 요청/응답 DTO
```
## 사전 준비
- JDK 21
- Docker, Docker Compose
- GitHub Package Registry 접근 권한
- 실행 환경에 맞는 Config Server, Eureka, Kafka, DB 설정

`org.pgsg:common` 패키지를 GitHub Package Registry에서 내려받기 때문에 Gradle 인증 정보가 필요합니다.

```properties
# ~/.gradle/gradle.properties
gpr.user=GITHUB_USERNAME
gpr.token=GITHUB_TOKEN
```

또는 환경변수로 지정할 수 있습니다.

```bash
export GPR_USER=GITHUB_USERNAME
export GPR_TOKEN=GITHUB_TOKEN
```

## 설정
로컬 또는 배포 환경에서는 다음 값이 필요합니다.

| 변수 | 설명 |
|---|---|
| `DB_URL` | PostgreSQL 접속 주소 (예: `pgsg-db:5432/chat-db`) |
| `DB_USERNAME` | 데이터베이스 사용자명 |
| `DB_PASSWORD` | 데이터베이스 비밀번호 |
| `DB_DDL_AUTO` | DDL 자동 실행 여부 (`update` 권장) |
| `EUREKA_URL` | Eureka 서버 URL |
| `SPRING_CLOUD_CONFIG_URI` | Config Server 주소 |
| `TRUST_STORE_PASSWORD` | Kafka SSL truststore 비밀번호 |

Kafka topic 설정은 Config Server에서 제공되어야 합니다.

| 설정 키 | 설명 |
|---|---|
| `topics.trade.created` | 거래 생성 이벤트 수신 topic |
| `topics.trade.completed` | 거래 완료 이벤트 수신 topic |
| `topics.trade.cancelled` | 거래 취소 이벤트 수신 topic |
| `topics.chat.roomCreated` | 채팅방 생성 이벤트 발행 topic |
| `topics.chat.messageSent` | 메시지 전송 이벤트 발행 topic |

## Docker 실행

```bash
# 외부 네트워크 생성
docker network create pgsg-network

# 인프라 실행 (루트 디렉토리에서)
docker-compose -f docker-compose.infra.yaml up -d

# 서비스 빌드 및 실행
docker-compose up -d --build
```

기본 포트 매핑은 다음과 같습니다.

| 대상 | 포트    |
|---|-------|
| Host | 19050 |
| Container | 8084  |

## API

### REST API

**채팅방 단건 조회**

```
GET /api/v1/chat/rooms/{roomId}
```

**채팅방 목록 조회**
```
GET /api/v1/chat/rooms
```
| 파라미터 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `userIds` | `List<UUID>` | N | 사용자 ID 목록 |
| `productName` | `String` | N | 상품명 검색 |
| `userName` | `String` | N | 사용자명 검색 |
| `keyword` | `String` | N | 통합 키워드 검색 |

### WebSocket STOMP

| 기능 | Method | Endpoint |
|---|---|---|
| WebSocket 연결 | CONNECT | `/chat` |
| 메시지 발송 | PUBLISH | `/app/{roomId}/message` |
| 메시지 수신 | SUBSCRIBE | `/topic/room/{roomId}` |

## 이벤트 흐름

### 수신 이벤트
`prod-trade-created` topic의 거래 생성 이벤트를 수신해 채팅방을 생성합니다.

```json
{
  "tradeId": "00000000-0000-0000-0000-000000000000",
  "productId": "00000000-0000-0000-0000-000000000000",
  "productName": "상품명",
  "sellerId": "00000000-0000-0000-0000-000000000000",
  "sellerNickName": "판매자",
  "buyerId": "00000000-0000-0000-0000-000000000000",
  "buyerNickName": "구매자"
}
```

`prod-trade-completed` topic의 거래 완료 이벤트를 수신해 채팅방을 완료 상태로 변경합니다.

`prod-trade-cancelled` topic의 거래 취소 이벤트를 수신해 채팅방을 취소 상태로 변경합니다.

### 발행 이벤트
서비스는 common 모듈의 Outbox 이벤트를 통해 다음 이벤트 등록을 요청합니다.

- 채팅방 생성: `prod-chat-room-created`
- 메시지 전송: `prod-chat-message-sent`

## 배포
운영 Compose 파일은 `deploy/docker-compose.prod.yaml`에 있습니다.

```bash
docker compose -f deploy/docker-compose.prod.yaml up -d
```