<div align="center">
  
## 💘 Heart Link _ 커플 SNS
검색 데이터 활용 타겟팅 광고 시스템
</div>

<br>

> Back-End
> <br>
> 2024.09.26 ~ 2024.11.10

<br>

### ✏️ Summary

##### '두 사람의 마음을 Heart(하트)로 Link(연결)한다' 는 의미의 커플만의 특별한 추억을 기록하고 공유할 수 있는 소셜 플랫폼입니다.
##### 기존의 SNS와 커플 앱을 통합한 새로운 형태의 커플 SNS를 통하여 다양한 커플 미션을 수행하며 관계를 깊이하고, 다른 커플들과도 따뜻한 소통을 나눌 수 있습니다.

<br>

### 🛠 ️Skills
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white) ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black) ![React](https://img.shields.io/badge/React-00008B?style=flat-square&logo=react&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white) ![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=flat-square&logo=elasticsearch&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=flat-square&logo=java&logoColor=white) ![eBay API](https://img.shields.io/badge/eBay%20API-FF0000?style=flat-square&logo=ebay&logoColor=white) ![DeepL API](https://img.shields.io/badge/DeepL%20API-00008B?style=flat-square&logo=deepl&logoColor=white)

<br>

### 👩‍💻 My Role
#### 🔍 검색 기능
##### 1. 검색 데이터 관련 광고 수집
- 영어 검색만 가능한 eBay 광고 API 특성으로 사용자의 검색기록을 DeepL API라는 번역 API를 통하여 번역
- 번역된 검색기록을 elastic search 인덱스에 저장
- 사용자가 메인페이지에서 피드 조회 시 최근 검색기록을 바탕으로 광고 API에서 관련 광고 데이터를 수집 및 피드 형식으로 노출
##### 2.  태그 자동완성
- 게시글 작성 시 태그들을 분석기와 토크나이저가 설정된 elastic search 인덱스에 저장
- 태그 검색 시 비슷한 태그들을 노출
##### 3. 아이디 자동완성
- 회원 가입 시 유저아이디, 유저의 로그인 아이디를 elastic search 인덱스에 저장
- 아이디 검색 시 비슷한 아이디들의 목록 노출
##### 4. 검색기록
- 사용자의 검색기록을 저장 후 검색창 아래에 노출
#### 🩷 커플 기능
##### 1. 커플 연결/해지
- 연결 시 회원가입 시 부여된 상대방의 랜덤코드를 입력 시 커플 연결됨.
- 커플 해지 신청 시 3개월 간의 유예기간 설정
- 유예기간 종료 후 최종 연결 해지
- 유예기간 없이 종료 희망 시 즉시 연결 해지
- 커플 해지 취소
##### 2. 커플 매치 (밸런스 게임)
- 매일 2개의 선택지를 사용자에게 노출 및 선택
- 모든 선택한 내역들 조회
- 일일 선택지에 대한 모든 커플들의 통계 그래프 조회
##### 3. 커플 미션 (미션 태그로 피드 작성)
- 매월 9개의 커플 미션 조회
- 해당 태그가 포함 된 피드 작성 시 미션 테이블에 작성된 피드 노출

<br>

### ❗️ Difficulties
#### 1. 광고 API 탐색 과정
###### 어려움
학생 팀 프로젝트이기 때문에 사업자 등록증이나 실제 판매 수익이 있는 상황이 아니어서 선택지가 매우 찾기 어려웠음.
###### 해결방안
꾸준히 탐색해본 결과 현실적으로 선택할 수 있는 eBay Finding API를 발견하여 사용하였음. 그러나 국내 사이트가 아닌 점으로 영어 검색만 가능하여 번역할 수 있는 DeepL API를 사용하여 번역 후 광고 데이터를 조회함.

#### 2. 태그 자동완성 시 검색 언어 고려
###### 어려움
태그를 검색 할 때 같은 내용이지만 언어가 달라 검색되지 않는 태그가 있을 경우를 고려하게 됨.
<br>
(ex. 'date' 태그 검색 -> 'date' 태그는 없지만 '데이트' 태그는 존재할 경우)
###### 해결방안
elastic search 인덱스에 원형 필드, 영어 필드, 한글 필드를 만들어 태그 저장 시 DeepL API를 활용하여 각자 언어로 번역된 태그에 맞게 필드에 저장 후 자동완성 기능 구현 시 검색어로 검색 후 조회 시 원형 필드를 조회해 오게 함.

<br>

### 📋 ERD & UseCases
<div align="center" style="display: inline-block;">

<img src="하트링크 ERD.jpg" alt="Project Logo" style="width: 47%; margin-right:10px; height: auto;" />
<img src="하트링크 유스케이스.png" alt="Project Logo" style="width: 47%; height: auto;" />
</div>
<br>

### 💡 Front-End
#### https://github.com/YeBook99/Heart-Link_frontend.git

