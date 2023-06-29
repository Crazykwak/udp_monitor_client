# udp_monitor_client
udp 통신을 통해서 표준 출력으로 뭔가를 볼 수 있는 클라이언트

### 환경설정
classpath : /lib

### 사용법

#### 1. 통신방법
 - UDP 통신 : 자체 포트는 가지고 있지 않고 일단 데이터를 보내줄 대상에게 ping 처럼 한 번 찔러봄. 그럼 거기서 받은 udp 포트로 ack 보내주듯 데이터 날리면 됨.
 - 흐름 : 모니터클라이언트 udp 찌르기 -> 데이터 보내줄 넘 (찔린 후 udp 통신으로 데이터를 보내줌) -> 모니터 클라이언트 (udp 리시브)

#### 2. 통신 데이터

Json 형식으로 컬럼, 값을 받음. 당연히 byte[] 로 통신함.

{
"colArray" : ["예시", "컬럼", "목록"],
"예시" : 값,
"컬럼" : 값,
"목록" : 값
}

colArray는 무적권임. 이 키가 없으면 아에 작동을 하지 않는다.
colArray의 밸류는 배열로

#### 3. 실행 환경변수

> java -DcolColor=30 -DcolBgColor=47 -Dcolor=37 -Dhost=127.0.0.1 -Dport=$PORT {경로}.com.odinues.m1customerApi.kbcard.Monitor

colColor = 컬럼 색
colBgColor = 컬럼 배경색
color = 값 글자색
host = 통신주소
port = 통신 포트 (찌를 애)


