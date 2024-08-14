## Class Naming Convention
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

## 모듈 구조
### 1. domain: 도메인 모듈

```text
- 비지니스 로직에서 해결하고자 하는 도메인 객체들
- 모든 모듈에서 사용하는 DTO, VO, Entity(JPA Enitty 아님) 객체의 모음
- 일단 포트 역할하는 인터페이스도 이 모듈에 담는다.
```

|             | app | adapter | core | support | domain |
|-------------|-----|---------|------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | -    | -       | -      |

### 2. support: 서포트 모듈

```text
- 프로젝트와는 독립적으로 동작 할 수 있는 객체들을 모아두는 모듈
- TimeUtis 등
```

|             | app | adapter | core | support | domain |
|-------------|-----|---------|------|---------|--------|
| 사용가능한 모듈 여부 | -   | -       | -    | -       | -      |

### 3. core: 코어 모듈

```text
- 비지니스 로직을 관리하는 모듈
- 웹 통신 / DB 관련 객체는 가급적 사용을 피한다.
```

|             | app | adapter | core | support | domain |
|-------------|---|---------|------|---------|--------|
| 사용가능한 모듈 여부 | - | Runtime | -    | O       | O      |


### 4. adapter : 외부 통신 모듈

```text
- DB나 타 서비스 등 다른 서비스와 통신 하는 모듈
- JPA / Kafka Producer / Http 통신 등이 해당 된다.
```

|             | app | adapter | core | support | domain |
|-------------|---|---|------|---------|--------|
| 사용가능한 모듈 여부 | - | - | - | O       | O      |

### 5. app : 요청 Receive 모듈

```text
- 서비스에 들어오는 요청을 처리하는 구현체
- Web Controller / Kafka Consumer 등이 해당 된다.
```

|             | app | adapter | core | support | domain |
|-------------|---|---|------|---------|--------|
| 사용가능한 모듈 여부 | - | - | O    | O       | O      |
