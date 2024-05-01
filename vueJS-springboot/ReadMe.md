
## 라이브러리 버전 정보
- node: 18.20.2
- npm: 10.5.0
- axios: 1.6.8 
- bootstrap: 5.3.3 
- element-plus: 2.7.2

## 실행 방법
```bash
# 가상환경 생성
nodeenv --node=18.20.2 nodeenv-18

# node 가상환경 활성화
. ./nodeenv-18/bin/activate

# 패키지 설치
npm intall
```
npm install 명령어를 통해서 설치하는 방법은 크게 2가지가 있다.
1. npm install 설치할 패키지명 => 이 방법은 특정 패키지를 설치할때 사용한다.
2. npm install => package.json에 정의된 모든 패키지들을 설치한다.


## vue 프로젝트 생성
```bash
# vue 프로젝트 생성
npm init vue@latest


# vue 프로젝트 실행
npm run dev
```