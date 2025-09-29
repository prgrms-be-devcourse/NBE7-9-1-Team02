# NBE7-9-1-Team02
프로그래머스 데브코스 7기 9회차 2팀 1차 프로젝트입니다.

## 코딩 컨벤션

### 🔸 패키지 이름

- 소문자
- 띄어쓰기 없음

### 🔸 클래스/인터페이스 이름

- 대문자 낙타 표기법(Upper camel case) = 파스칼 표기법(Pascal Case)
- 첫 글자 대문자, 연결되는 단어 첫 글자 대문자
- 명사, 명사절로 짓는다
- 형용사/형용사절(인터페이스만)

```java
public class AccessToken
```

### 🔸 메서드 이름

- 소문자 낙타 표기법(Lower Camel Case)
- 첫 번째 단어 소문자

```java
toString()
renderHtml()
withUserId(String id)
```

### 🔸 변수

- 소문자 낙타 표기법(Lower Camel Case)

```java
private int accessToken;
```

### 🔸 **애너테이션 선언 후 새줄 사용**

- 클래스, 인터페이스, 메서드, 생성자에 붙는 애너테이션은 선언 후 새줄을 사용

```java
@RequestMapping("/guests")
public void findGuests() {}

@Override public void destroy() {}
```

### 🔸 **주석문 기호 전후의 공백 삽입**

- 주석의 전후에는 아래와 같이 공백을 삽입
```java
/* 주석내용 앞에 공백, 에도 공백 */
```
