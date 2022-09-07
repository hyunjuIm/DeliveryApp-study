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

<br>

> ## 주요기능 및 결과화면
❗️ Backendless로 개발하여 Mock data가 섞여있습니다.

