<div align="center">

![delivery-man (2)](https://user-images.githubusercontent.com/68545018/188453349-bf4cb5d2-6c1d-4911-ace7-791a323d1ccb.png)
# 배달앱<br><img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white"/> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/>

</div>

<br>

> ## 아키텍쳐

![무제](https://user-images.githubusercontent.com/68545018/188452754-466d42af-13f8-4577-8953-16a13802ef50.png)

<br>

> ## 사용스택
- MVVM, LiveData (+ State 패턴)
- Android Jetpack : Room, CameraX
- Coroutine
- Retrofit2
- Firebase
- Koin

<br>

> ## 깨달음

- 리뷰 사진 리스트를 Firebase 서버로 업로드 하는 동안 대용량 사진으로 인해 길어지는 업로드 시간이 사용자에게 오류로 인식될 것이라는 생각이 들었다. (이전에 백엔드 서버에 업로드할 때도 사진 용량 때문에 413(Request Entity Too Large) 에러를 겪었던 경험이 있다.)<br>
**➡️ Coroutine 비동기 처리를 통해 최적화(Resize) 된 Bitmap을 반환 받아 서버에 올릴 용량과 업로드 시간을 단축할 수 있었다.**<br>
[📝 기술 블로그 : 안드로이드 Bitmap 최적화(Resize)한 다중 이미지 서버에 업로드하기](https://velog.io/@dear_jjwim/series/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-Bitmap-%EC%B5%9C%EC%A0%81%ED%99%94Resize%ED%95%9C-%EB%8B%A4%EC%A4%91-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%84%9C%EB%B2%84%EC%97%90-%EC%97%85%EB%A1%9C%EB%93%9C%ED%95%98%EA%B8%B0)

- 그동안 MVC 패턴으로 코딩하면서 프로젝트가 커질수록 지저분해지고 보일러 플레이트 코드도 많았었다. 하지만 **MVVM 패턴을 적용하니 LiveData 덕분에 View와 비즈니스 로직의 분리가 훨씬 편해졌다.** 상태(Loading, Success, Error 등)에 따라 행동이 변화하는 객체는 `Seald Class`를 사용해 State 패턴으로 관리하니 좀 더 직관적이고 가독성 있는 코드를 작성할 수 있었다❗️<br>
[📝 기술 블로그 : MVC, MVP, MVVM 패턴 (+ DI)](https://velog.io/@dear_jjwim/MVC-MVP-MVVM-%ED%8C%A8%ED%84%B4-DI)

- RecyclerView를 사용할 때, 데이터를 많이 가진 리스트(주변 식당 데이터, 갤러리 사진들 등)를 변경하는 작업에서 데이터 하나만 바뀌어도 리스트 전체를 업데이트 하는 `notifyDataSetChanged()` 호출이 얼마나 비효율적인지 몸소 깨달았다. 이제는 oldItem, newItem의 두 데이터셋을 비교하여 값이 변경된 부분만 RecyclerView에게 알려주는 `DiffUtil`을 사용할 것이다.

- 테스트 코드를 처음 작성해봤다. 잘못 작성된 부분을 빠르게 확인할 수 있고, 디버깅 및 기기를 통해 UI로 직접 입력하는 시간을 단축할 수 있어 좋았다. 프로젝트가 커질수록 안정성과 신뢰성이 높아지는데 큰 기여를 할 것 같다. 하지만 오류도 참 많이 만났다. 아직은 어려워서 더 열심히 공부해야겠다. 사용하다보니 왜 의존성 없는 코드를 작성해야하는지 알겠다.<br>
[📝 기술 블로그 : 안드로이드 UnitTest에서 LiveData 사용시 오류 - Method getMainLooper in android.os.Looper not mocked.](https://velog.io/@dear_jjwim/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-UnitTest%EC%97%90%EC%84%9C-LiveData-%EC%82%AC%EC%9A%A9%EC%8B%9C-%EC%98%A4%EB%A5%98-Method-getMainLooper-in-android.os.Looper-not-mocked)

<br>

> ## 결과화면
❗️ Backendless로 개발하여 Mock data가 섞여있습니다.
|메인화면 - 홈|위치 변경|메인화면 - 찜|
|------|---|---|
|![Screenshot_20220915-235712_DeliveryApp](https://user-images.githubusercontent.com/68545018/190451011-d418661e-c873-4149-8fdc-2bd4e65d12b9.jpg)|![Screenshot_20220915-235842_DeliveryApp](https://user-images.githubusercontent.com/68545018/190451565-fed590c9-688b-4864-aee2-3882ce1ddc85.jpg)|![Screenshot_20220916-000110_DeliveryApp](https://user-images.githubusercontent.com/68545018/190452344-fac7955c-b623-43d2-8712-62b8a5d86eca.jpg)|

|식당 상세 화면|식당 상세 화면 (스크롤시 상단바)|식당 상세 화면 (리뷰)|
|------|---|---|
|![Screenshot_20220915-235946_DeliveryApp](https://user-images.githubusercontent.com/68545018/190452948-a8a22d06-dfcb-41f7-bed1-d71bcb04de02.jpg)|![Screenshot_20220916-000051_DeliveryApp](https://user-images.githubusercontent.com/68545018/190453009-722bb3ce-8dfd-4d72-8e4b-bd927321ffc9.jpg)|![Screenshot_20220916-005134_DeliveryApp](https://user-images.githubusercontent.com/68545018/190453072-a2a3cacd-ebaa-4e99-9036-f91d0a9dcfc4.jpg)|

|리뷰 작성|리뷰 사진 첨부 - 갤러리|리뷰 사진 첨부 - 카메라|
|------|---|---|
|![Screenshot_20220916-005012_DeliveryApp](https://user-images.githubusercontent.com/68545018/190453593-344513d4-5ad6-4cd9-9e09-d756b139f4e3.jpg)|![image](https://user-images.githubusercontent.com/68545018/190454352-444e195c-7f48-4ec1-ad65-bfe6f74eb223.png)|![Screenshot_20220916-005005_DeliveryApp](https://user-images.githubusercontent.com/68545018/190453637-fad77de9-42e2-4c90-8f3e-8070d8563685.jpg)

|장바구니 및 주문|메인화면 - My (로그인 전)|메인화면 - My (로그인 후)|
|------|---|---|
|![Screenshot_20220916-000001_DeliveryApp](https://user-images.githubusercontent.com/68545018/190454820-bb427ccd-7309-4b78-83f5-e9dcbfeaed89.jpg)|![Screenshot_20220916-000114_DeliveryApp](https://user-images.githubusercontent.com/68545018/190454861-6f40ef15-c7cb-4655-8b4d-b68c4ea9bacd.jpg)|![Screenshot_20220916-004812_DeliveryApp](https://user-images.githubusercontent.com/68545018/190454889-f94a773a-a8ea-4fbe-962b-22e5af8fbf04.jpg)|


