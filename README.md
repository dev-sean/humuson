## 프로젝트 개요

이 프로젝트는 주문 관리 시스템을 설계하고 외부 시스템과의 데이터 연동을 위한 인터페이스를 구현하는 것이 목적입니다. 주요 기능은 외부 시스템과 주문 데이터를 주고받는 것입니다

## 기술 스택

- 언어: Kotlin
- 프레임워크: Spring Boot 3.4
- Java 버전: 21
- (필수 아님) Node.js (외부 시스템 서버용)
- (필수 아님) npm (Node.js 패키지 관리자)

## 프로젝트 구조

```  
com.humuson.devsean  
├── common  
│   ├── exception  
│   └── support  
├── configuration  
├── controller  
├── dto  
├── entity  
├── repository  
└── service  
```  

1. `common`: 공통적으로 사용 되는 기능들을 포함하는 패키지
    - `exception`: 애플리케이션에서 발생할 수 있는 커스텀 예외 클래스들을 정의
    - `support`: 여러 곳에서 사용되는 유틸리티 함수들을 포함
2. `configuration`: 애플리케이션의 설정 관련 클래스들을 포함하는 패키지
3. `controller`: API 요청을 처리하는 컨트롤러 클래스들을 포함하는 패키지
4. `dto`: 계층 간 데이터 전송에 사용되는 객체들을 정의
5. `entity`: 메모리 저장용 클래스들을 포함하는 패키지
6. `repository`: 데이터 접근 계층을 담당하는 인터페이스나 클래스들을 포함하는 패키지
    - 데이터의 저장, 조회, 수정, 삭제 등의 작업을 수행
7. `service`: 비즈니스 로직을 처리하는 서비스 클래스들을 포함하는 패키지

## 주요 기능

1. 외부 시스템으로부터 JSON 형식의 주문 데이터를 HTTP를 통해 수신(`ExternalSystemConnectorImpl` 클래스 참조)
    - RestTemplate: 외부 시스템과의 HTTP 통신을 위해 사용되었습니다.
    - 코루틴: 외부 시스템과의 HTTP 통신 실패 시 일정 횟수 동안 재시도 하기 위해서 사용되었습니다.
2. 수신한 주문 데이터를 내부 시스템에 저장
3. 주문 데이터를 JSON 형식으로 변환하여 외부 시스템으로 전송
    - JSON 데이터 변환 로직이 구현되어 있습니다. (`ExternalDataConverterImpl` 클래스 참조)
4. 주문 ID(`orderId`)를 통한 데이터 조회
5. 저장된 주문 데이터 리스트 형식 반환
6. 커스텀 예외 처리
    * 커스텀 예외 클래스(`ExternalSystemException`, `DataValidationException`, `HttpClientException`)를 정의하여 사용했습니다.
    * 세분화된 예외 처리로 문제의 정확한 원인을 쉽게 파악할 수 있습니다.
7. 인터페이스 설계
    * 인터페이스 기반의 설계를 통해 기존 코드의 수정 없이 새로운 외부 시스템과의 즉각적인 통합이 가능합니다.
        * `OrderRepository`: 주문 데이터의 저장 및 조회 로직을 추상화합니다.
        * `HttpClient`: HTTP 통신의 추상화 계층을 제공합니다.
        * `ExternalSystemConnector`: 외부 시스템과의 통신을 추상화합니다.
        * `ExternalDataConverter`: 외부 시스템과 내부 시스템 간의 데이터 형식 변환을 담당합니다.

## 클래스 다이어그램

[![](https://mermaid.ink/img/pako:eNrNVt9v2jAQ_lciP1ENUAPlxyLE1NFqYyrbVLKXiRc3OcCaY0e2UzXt-N93cUibQKBI07TmAWLffXefz1_OfiKBDIF4JOBU6ytGV4pGC-HgY2ec6wcDSlD-TYWgrox0nnJr9ryT2eQ09Jy5UUysSpYg0UZGoL7SCGrMFnhFTZ1NG2oSXTVs8r8yMUuowob9HZEbGVCevfosgho-NuHcDp5J7RG6hVhqZqRKy9RGIyawjEsawHhcDk3voWEpYPwbps3IBhmflXyWTIQf02nY2Cn22ZbRhx3fS84bZ5VotWynYgYR0jzCulUwmyWG3nGY0XiU52465cj_eymFROepNhBNpBAQ4FpO2YAlmGBdkbhulBlrEGE-7ctqmsLtNC7TKOaV0sLW7zJmn8D8UHxfm2Wf71IfcFJgVJopFss-FaZkCYHTdMY4Z9mOyIrs_9m68fuhuOp7UAZO2oEgd_alTZcrA9uMt9d4CpHUYQvfL1qKWhHudZNXue_u2Fsg-tmYeMIZCHNKZVdgGsmLZpqOAh1LocFPY2x3kyzkyM9S-iUUNoId2J0MU8-5FOmRCLdbw7UwzKQ4V8sfvYwPWFhssvVrQTm_uHgVwBtd2aE-2mr9Hu8eCTniQBUs4GWc-x5rJxZwwKGK3hd2BVsxv4Zst8f7dwIPRZjoZ_kdy7stioUECrAGGaqoZvFLmgTP7IiyEK8mVh8LYtaA5zLx8DWk6teCLMQG_Whi5DwVAfGMSqBJlExWa-ItKdc4SuIQU2zvNc-zMRU_pYwKCA6J90QeiNdyz9222-33eu-H_UFvOLxokpR4w2G7g3Nud9g_H3QGrrtpkkcbwG0POp2LXv-i1-t0zwdu1938AfCZBAM?type=png)](https://mermaid.live/edit#pako:eNrNVt9v2jAQ_lciP1ENUAPlxyLE1NFqYyrbVLKXiRc3OcCaY0e2UzXt-N93cUibQKBI07TmAWLffXefz1_OfiKBDIF4JOBU6ytGV4pGC-HgY2ec6wcDSlD-TYWgrox0nnJr9ryT2eQ09Jy5UUysSpYg0UZGoL7SCGrMFnhFTZ1NG2oSXTVs8r8yMUuowob9HZEbGVCevfosgho-NuHcDp5J7RG6hVhqZqRKy9RGIyawjEsawHhcDk3voWEpYPwbps3IBhmflXyWTIQf02nY2Cn22ZbRhx3fS84bZ5VotWynYgYR0jzCulUwmyWG3nGY0XiU52465cj_eymFROepNhBNpBAQ4FpO2YAlmGBdkbhulBlrEGE-7ctqmsLtNC7TKOaV0sLW7zJmn8D8UHxfm2Wf71IfcFJgVJopFss-FaZkCYHTdMY4Z9mOyIrs_9m68fuhuOp7UAZO2oEgd_alTZcrA9uMt9d4CpHUYQvfL1qKWhHudZNXue_u2Fsg-tmYeMIZCHNKZVdgGsmLZpqOAh1LocFPY2x3kyzkyM9S-iUUNoId2J0MU8-5FOmRCLdbw7UwzKQ4V8sfvYwPWFhssvVrQTm_uHgVwBtd2aE-2mr9Hu8eCTniQBUs4GWc-x5rJxZwwKGK3hd2BVsxv4Zst8f7dwIPRZjoZ_kdy7stioUECrAGGaqoZvFLmgTP7IiyEK8mVh8LYtaA5zLx8DWk6teCLMQG_Whi5DwVAfGMSqBJlExWa-ItKdc4SuIQU2zvNc-zMRU_pYwKCA6J90QeiNdyz9222-33eu-H_UFvOLxokpR4w2G7g3Nud9g_H3QGrrtpkkcbwG0POp2LXv-i1-t0zwdu1938AfCZBAM)

1. **OrderRepository (Interface)**
    - 주문 데이터를 저장하고 조회하는 역할을 위한 인터페이스를 정의합니다.
        - `save`: 데이터를 저장합니다.
        - `findById`: `orderId`를 통해 데이터를 조회합니다.
        - `findAll`: 모든 데이터를 리스트 형식으로 조회합니다.
    - 데이터 저장 방식을 쉽게 변경할 수 있습니다. 현재는 `InMemoryRepository`로 구현되어 있지만, 향후 데이터베이스나 클라우드 스토리지 등으로 변경이 필요할 때 `OrderRepository`
      인터페이스를 구현하는 새로운 클래스만 작성하면 됩니다.

2. **InMemoryRepository**
    - `OrderRepository` 인터페이스의 구현체입니다.
    - 데이터 저장 및 주문 조회 로직을 구현합니다.
    - 모든 데이터를 메모리에 저장합니다.
    - 애플리케이션 종료 시 저장된 데이터가 모두 삭제됩니다.
    - HashMap을 사용하여 메모리에 데이터를 저장하고 관리합니다.

3. **ExternalSystemConnector (Interface)**
    - 외부 시스템과의 통신을 위한 인터페이스를 정의합니다.
    - 주문 데이터 조회 및 전송을 위한 표준화된 메서드를 제공합니다.
        - `fetchExternalOrders`: 외부 시스템에서 데이터를 가져옵니다.
        - `sendOrdersToExternalSystem`: 내부 시스템에서 외부 시스템으로 데이터를 내보냅니다.
    - 다양한 외부 시스템과의 연동을 용이하게 합니다. 새로운 외부 시스템이 추가될 때마다 `ExternalSystemConnector` 인터페이스를 구현하는 새로운 클래스를 작성하면 됩니다. 이를 통해 각 외부
      시스템의 특성에 맞는 통신 로직을 개별적으로 구현할 수 있습니다.

4. **ExternalSystemConnectorImpl**
    - `ExternalSystemConnector` 인터페이스의 구현체입니다.
    - 외부 시스템과의 실제 통신을 담당하는 구현 클래스입니다.
    - 다음과 같은 주요 속성을 포함합니다
        - `externalApiGetUrl`: 외부 API GET 요청 URL
        - `externalApiPostUrl`: 외부 API POST 요청 URL
        - `retryTimes`: 재시도 횟수
        - `delayMillis`: 재시도 간 지연 시간
    - 재시도 로직(`retryTimes`, `delayMillis`)을 통해 일시적인 통신 오류에 대응합니다.

5. **ExternalDataConverter (Interface)**
    - 외부 시스템의 데이터 형식과 내부 시스템의 데이터 형식 간 변환을 담당합니다.
    - 데이터 포맷 변환의 표준화된 인터페이스를 제공합니다.
        - `convertToOrder`: 외부 데이터를 내부 데이터 형식에 맞게 변환합니다.
        - `convertToExternalJson`: 내부 데이터(`Order`)를 외부로 내보내기 위해 JSON 형식으로 변환합니다.
    - 다양한 외부 시스템의 데이터 형식에 대응할 수 있습니다. 새로운 외부 시스템이 추가될 때마다 `ExternalDataConverter` 인터페이스를 구현하는 새로운 클래스를 작성하여 해당 시스템의 데이터
      형식을 내부 형식으로 변환할 수 있습니다.

6. **ExternalDataConverterImpl**
    - `ExternalDataConverter` 인터페이스의 구현체입니다.
    - 실제 데이터 변환 로직을 포함합니다.
    - 외부 주문 데이터(`ExternalOrderDto`)를 내부 형식(`Order`)으로, 내부 주문 데이터(`Order`)를 JSON으로 변환합니다.

7. **HttpClient (Interface)**
    - HTTP 통신을 위한 기본적인 인터페이스를 정의합니다.
        - `get`: HTTP GET 요청을 처리합니다.
        - `post`: HTTP POST 요청을 처리합니다.
    - 다양한 HTTP 클라이언트 라이브러리(예: OkHttp, Apache HttpClient)를 사용할 수 있습니다. 현재는 RestTemplate을 사용하고 있지만, 다른 라이브러리로 변경하고자 할 때
      `HttpClient` 인터페이스를 구현하는 새로운 클래스만 작성하면 됩니다.

8. **RestTemplateHttpClient**
    - `HttpClient` 인터페이스의 구현체입니다.
    - RestTemplate을 사용하여 실제 HTTP 통신을 구현합니다.
    - HTTP 헤더 설정 및 응답 처리를 담당합니다.
    - 통신 오류에 대한 예외 처리를 수행합니다.

9. **ExternalOrderDto**
    - 외부 시스템과의 데이터 교환을 위한 데이터 전송 객체입니다.
    - 다음과 같은 정보를 포함합니다.
        - `orderId`(String): 주문 식별자
        - `customerName`(String): 고객명
        - `orderDate`(String): 주문 일자
        - `status`(String): 주문 상태

10. **Order**
    - 내부 시스템에서 사용하는 주문 객체입니다.
    - 다음과 같은 정보를 포함합니다.
        - `orderId`(String): 주문 식별자
        - `customerName`(String): 고객명
        - `orderDate`(LocalDateTime): 주문 일자
        - `status`(OrderStatus): 주문 상태

## 컴포넌트 간의 관계

1. **데이터 저장 계층**
    - `OrderRepository` 인터페이스를 `InMemoryRepository`가 구현하여 데이터 저장소를 추상화합니다.

2. **외부 시스템 통신 계층**
    * `RestTemplateHttpClient`가 `HttpClient`를 구현하여 실제 HTTP 통신을 수행합니다:

    - `ExternalSystemConnector` 인터페이스를 `ExternalSystemConnectorImpl`이 구현하여 외부 시스템과의 통신을 담당합니다.

3. **데이터 변환 계층**
    - `ExternalDataConverter` 인터페이스를 `ExternalDataConverterImpl`이 구현하여 데이터 형식 변환을 담당합니다.
    - `ExternalOrderDto`와 `Order` 객체 간의 변환을 수행하여 외부/내부 시스템 간 데이터 호환성을 보장합니다.

## 예외 처리

이 프로젝트는 세 가지 주요 커스텀 예외 클래스를 사용하여 다양한 오류 상황을 관리합니다. 예외 발생시 로그도 함께 남깁니다. 세분화된 예외 처리로 문제의 정확한 원인을 파악할 수 있고 일관된 로깅으로 문제
해결과  
모니터링을 용이하게 합니다.

1. **ExternalSystemException**
    - 외부 시스템과의 통신 중 발생하는 예외를 처리합니다.
    - `ExternalSystemConnectorImpl` 클래스에서 발생하는 예외를 처리할때 사용됩니다.

2. **DataValidationException**
    - 데이터 유효성 검사 과정에서 발생하는 예외를 처리합니다.
    - `validateExternalOrderData` 함수에서 발생하는 예외를 처리할때 사용됩니다.

3. **HttpClientException**
    - HTTP 클라이언트 관련 예외를 처리합니다.
    - `RestTemplateHttpClient` 클래스에서 발생하는 예외를 처리할때 사용됩니다.

## 실행 방법

### 애플리케이션 설정

`src/main/resources/application.properties`파일에 `server.port`, `external.api.get-url`, `external.api.post-url`을 필요에 따라
수정하세요.

```text  
spring.application.name=devsean  
server.tomcat.relaxed-query-chars=[,],{,},^,|  
# HTTP 통신 실패시 재시도 횟수 및 시간  
retry.times=3  
retry.delay-millis=3000  
# 수정 필요  
server.port=3000  
external.api.get-url=http://localhost:3001/orders  
external.api.post-url=http://localhost:3001/orders  
```  

### 외부 시스템 서버 설정

1. 프로젝트 루트 디렉토리에`server.js`파일이 있는지 확인하세요.
2. `order.json`파일을`server.js`와 같은 디렉토리에 위치시키세요.
3. 터미널에서 다음 명령어를 실행하여 필요한 패키지를 설치하세요.

```bash  
npm install express
```  

4. 외부 시스템 서버를 실행하세요.

```bash  
node server.js
```  

서버가 정상적으로 실행되면 "Server running at [http://localhost:3001](http://localhost:3001/)" 메시지가 표시됩니다.

### 주문 관리 시스템 실행

1. 프로젝트를 빌드하고 실행하세요.
2. 애플리케이션이 성공적으로 시작되면, 다음 API를 순서대로 호출하여 시스템을 테스트할 수 있습니다.

외부 시스템에서 주문 데이터 가져오기

```  
GET http://localhost:3000/api/orders/external  
```  

주문 데이터 조회

```  
GET http://localhost:3000/api/orders/{orderId}  
```  

모든 주문 목록 조회

```  
GET http://localhost:3000/api/orders/list  
```  

주문 데이터를 외부 시스템으로 전송

```  
POST http://localhost:3000/api/orders/external  
```  

* 주의사항
    - 외부 시스템 서버(`server.js`)를 먼저 실행한 후, 주문 관리 시스템을 실행하세요.
    - API 호출 시, 먼저 외부 시스템에서 데이터를 가져오는 API(`/api/orders/external`)를 호출한 후 다른 API를 사용하세요.

## 코드 품질 관리

### 코드 컨벤션

이 프로젝트는 Kotlin 공식 코딩 컨벤션을 따르며, ktlint를 사용하여 코드 스타일을 일관되게 유지합니다.

### 단위 테스트

프로젝트의 신뢰성을 보장하기 위해 JUnit을 사용한 단위 테스트를 작성했습니다. 주요 테스트 클래스는 다음과 같습니다.

1. `DataValidationSupportTest`: 주문 데이터 유효성 검사 로직을 테스트합니다.
    - 주문 ID, 고객 이름, 주문 날짜 등의 유효성 검사
    - 잘못된 입력에 대한 예외 발생 확인

2. `InMemoryOrderRepositoryTest`: 메모리 기반 주문 저장소의 기능을 테스트합니다.
    - 단일 및 다중 주문 저장
    - ID를 통한 주문 조회
    - 전체 주문 목록 조회
    - 중복 주문 저장 시 덮어쓰기 동작 확인

3. `SupportTest`: 유틸리티 함수의 동작을 테스트합니다.
    - 문자열 날짜를 LocalDateTime으로 변환하는 기능 테스트
    - 잘못된 날짜 형식에 대한 예외 처리 확인

테스트 실행 방법

```bash  
./gradlew test
```