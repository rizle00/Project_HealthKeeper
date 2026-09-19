# Project_HealthKeeper

Android 앱(`App`)과 Spring 서버(`Spring/healthkeeper`, `mid`) 프로젝트입니다.

## 로컬 설정

실제 키와 비밀번호는 Git에 추가하지 않습니다. 예전 저장소에 있던 값은 재사용하지 말고 새로 발급한 값을 사용하세요.

- `info.properties.example`을 사용할 서버 모듈의 `src/main/resources/info.properties`로 복사하고 카카오·네이버·메일 설정을 입력합니다.
- Firebase 메시지를 사용하려면 새 서비스 계정 키를 해당 서버 모듈의 `src/main/resources/serviceAccountKey.json`에 둡니다. 현재 코드는 이 classpath 경로를 읽습니다. 이 파일이 포함된 서버 빌드 결과물도 공개하면 안 됩니다.
- `Spring/healthkeeper`는 실행 환경의 `DB_URL`, `DB_USER`, `DB_PASSWORD`로 DB에 접속합니다. `DB_URL`은 `jdbc:log4jdbc:oracle:thin:@localhost:1521:xe` 같은 log4jdbc URL입니다. JDBC 연결 테스트도 같은 환경변수를 사용합니다.
- `mid`는 `src/main/resources/dbconn/db.properties.example`을 같은 폴더의 `db.properties`로 복사한 뒤 DB 접속정보를 입력합니다.
- Android Firebase 설정은 해당 앱용 `google-services.json`을 `App/app/`에 둡니다. 서비스 계정 비공개 키를 Android 앱에 넣지 않습니다.

## 보안 정리 상태

현재 파일에서 서비스 계정 키·소셜 로그인 시크릿·메일 및 DB 접속정보와 빌드 사본을 제거하고 비밀번호 로그 출력을 삭제했습니다. 예시 파일에는 비밀값을 넣지 않습니다.

과거 Git 이력의 정리 여부와 별개로, 이미 노출된 서비스 계정 키는 Google Cloud IAM에서 폐기하고 사용 중인 관련 시크릿·비밀번호를 교체해야 합니다. 이 저장소 수정은 외부 서비스의 키를 폐기하거나 변경하지 않습니다.
