# Trouble Painter 🪄

![1](https://github.com/user-attachments/assets/e39f0b35-511d-4499-92a2-043c2239f872)

![2](https://github.com/user-attachments/assets/f4ef15cf-a16b-4b44-ad99-2e18ec28851b)

![3](https://github.com/user-attachments/assets/65b07113-7c5f-4532-aae6-a62e89ee6ab6)

![4](https://github.com/user-attachments/assets/1d3311e2-f687-485c-a123-64ea25b4fecc)

<br>

## Architecture ✨

<div align=center>

<img width="700" src="https://github.com/user-attachments/assets/6a16defc-611a-4671-8c98-1998a461344a">

</div>

<br>

## Teck Stack ✨

| IDE            | IntelliJ                                       |
|:---------------|:-----------------------------------------------|
| Language       | Kotlin                                         |
| Framework      | Spring Boot 3.2.5, Gradle                      |
| Authentication | Spring Security, JSON Web Tokens, Opaque Token |
| Orm            | Spring Data JPA                                |
| Database       | MySQL                                          |
| External       | Nginx, Docker, Redis, Kubernetes, ELK          |
| Monitoring     | Prometheus, Grafana, Sentry                    |
| CI/CD          | ArgoCD, Github Action                          |
| API Docs       | Notion, Swagger                                |
| Other Tool     | Jira, Discord, Postman, Figma                  |

<br>

## Class Naming Convention ✨

<table>
    <tr>
        <th>모듈</th>
        <th>접미어</th>
        <th>설명</th>
    </tr>
    <tr>
        <td rowspan="2">Domain</td>
        <td>UseCase</td>
        <td>In Port 인터페이스 </td>
    </tr>
    <tr>
        <td>Repository</td>
        <td>Out Port 인터페이스</td>
    </tr>
</table>

<br>

## Module Structure ✨

### domain: 도메인 모듈

```text
- 비지니스 로직에서 해결하고자 하는 도메인 객체들
- 모든 모듈에서 사용하는 DTO, VO, Entity(JPA Enitty 아님) 객체의 모음
- 일단 포트 역할하는 인터페이스도 이 모듈에 담는다.
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | -    | -     | -       | -      |

### support: 서포트 모듈

```text
- 프로젝트와는 독립적으로 동작 할 수 있는 객체들을 모아두는 모듈
- TimeUtis 등
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | -    | -     | -       | -      |

### event: 이벤트 모듈

```text
- 이벤트 모듈
- 객체간 연결을 느슨하게 하기 위한 객체
- 이벤트 발생 및 전달을 주로 한다
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | Runtime | -    | -     | O       | O      |

### core: 코어 모듈

```text
- 비지니스 로직을 관리하는 모듈
- 웹 통신 / DB 관련 객체는 가급적 사용을 피한다.
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | Runtime | -    | O     | O       | O      |

### adapter : 외부 통신 모듈

```text
- DB나 타 서비스 등 다른 서비스와 통신 하는 모듈
- JPA / Kafka Producer / Http 통신 등이 해당 된다.
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | -    | O     | O       | O      |

### app : 요청 Receive 모듈

```text
- 서비스에 들어오는 요청을 처리하는 구현체
- Web Controller / Kafka Consumer 등이 해당 된다.
```

|             | app | adapter | core | event | support | domain |
|-------------|-----|---------|------|-------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | O    | O     | O       | O      |
