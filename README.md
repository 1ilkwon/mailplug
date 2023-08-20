# mailplug

## 프로젝트 구조

mailplug/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── mailplug.homework/
│   │   │   │   │   ├── HomeworkApplication.java
│   │   │   │   │   ├── domain/
│   │   │   │   │   │   ├── ResponseMessage.java
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── BoardController.java
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── Board.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── BoardRepository.java
│   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── BoardService.java
│   ├── resources/
│   │   ├── application.properties
│   │   ├── static/
│   │   ├── templates/
│   ├── test/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── mailplug.homework/
│   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── BoardServiceTest.java
│   │   │   │   ├── HomeworkApplicationTests.java


## 개발 환경

- Java: 11
- MySQL: 8.0.33
- Spring Boot: 2.7.14
- IDE: IntelliJ IDEA
- Postman

## 구현 방법 설명

### 게시글 작성
1. 사용자가 게시글 정보(게시글 제목, 게시글 내용, 게시글 카테고리)를 입력하면 `BoardController`의 `registerPost` 메서드가 호출됩니다.
2. `BoardService`에서 게시글 정보의 유효성을 검사하고, 리포지토리를 통해 회원을 등록합니다.
3. 등록된 게시글 정보를 응답으로 프론트엔드에게 보냅니다.
4. `registerPost`에서 작성자 ID를 @RequestHeader를 통해 ,HTTP Header의 X-USERID 값을 받아와 게시글 수정 및 삭제를 위한 작성자를 지정합니다.

### 게시글 단건 조회
1. 사용자가 게시글의 ID를 요청 파라미터로 전달하면 `BoardController`의 `postDetailGet` 메서드가 호출됩니다.
2. 해당 ID로 `BoardService`에서 게시글을 조회하고, 조회 수를 증가시킵니다.
3. 조회된 게시글 정보를 응답으로 프론트엔드에게 보냅니다.

### 게시글 수정
1. 사용자가 게시글의 ID와 수정할 내용, @RequestHeader를 통해 받은 작성자 ID을 요청 파라미터로 전달하면 `BoardController`의 `updatePost` 메서드가 호출됩니다.
2. `BoardService`에서 해당 ID의 게시글을 조회하고, 작성자 ID와 비교하여 수정 권한을 확인합니다.
3. 권한이 확인되면 게시글 정보를 업데이트하고 리포지토리를 통해 수정합니다.
4. 수정 결과를 응답으로 프론트엔드에게 보냅니다.

### 게시글 삭제
1. 사용자가 게시글의 ID와 @RequestHeader를 통해 받은 작성자 ID를 요청 파라미터로 전달하면 `BoardController`의 `deletePost` 메서드가 호출됩니다.
2. `BoardService`에서 해당 ID의 게시글을 조회하고, 작성자 ID와 비교하여 삭제 권한을 확인합니다.
3. 권한이 확인되면 리포지토리를 통해 게시글을 삭제합니다.
4. 삭제 결과를 응답으로 프론트엔드에게 보냅니다.

### 게시글 목록
1. 사용자가 페이지 번호와 페이지 크기를 요청 파라미터로 전달하면 `BoardController`의 `postListGet` 메서드가 호출됩니다.
2. `BoardService`에서 페이지 번호와 크기를 기반으로 게시글 목록을 조회합니다.
3. 조회된 게시글 목록과 페이징 정보를 응답으로 프론트엔드에게 보냅니다.

### 게시글 제목 검색
1. 사용자가 제목 키워드와 페이지 번호, 페이지 크기를 요청 파라미터로 전달하면 `BoardController`의 `searchPost` 메서드가 호출됩니다.
2. `BoardService`에서 제목 키워드를 기반으로 페이지 번호와 크기를 사용하여 게시글을 검색합니다.
3. 검색된 게시글 목록과 페이징 정보를 응답으로 프론트엔드에게 보냅니다.

## 빌드 및 실행

1. 파일의 압축을 풀어줍니다.

2. MySQL 데이터베이스를 설정합니다. (예: 사용자 이름, 비밀번호, 데이터베이스 이름 등)

3. `application.properties` 파일을 열고 데이터베이스 설정과 포트번호를 변경합니다.

spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
spring.datasource.username=your-username
spring.datasource.password=your-password
server.port=8080

4. 프로젝트를 IntelliJ IDEA에서 열고 실행합니다.

5. Postman에서 `http://localhost:8080`에 접속하여 프로젝트를 확인합니다.



