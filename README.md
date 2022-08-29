# APIPracticeApp

## 概要
Githubのランキング一覧を表示するアプリです。

<img src = "https://user-images.githubusercontent.com/83356340/187214994-0ee02fd5-5cc2-4dc2-b61f-81f65892d909.gif" width = "400">


## 使い方
- 更新ボタンを押すことで、ランキングの更新を行うことができます。
- ランキングのリストをタップすることで、リポジトリの詳細を見ることができます。

## アーキテクチャ
[最新のアーキテクチャガイド](https://developer.android.com/jetpack/guide?hl=ja)に従って以下の構成にしています。(domainは省略)
<img src = "https://user-images.githubusercontent.com/83356340/187210959-3817c82f-749c-4897-b63e-0d591cb61812.jpg" width = "800">


## ライブラリ
インターンで使用するライブラリ+画像表示用のcoilを使用しています。

[ネットワーク通信]
- Retrofit
- OkHttp

[パース]
- Moshi

[UI]
- LiveData
- VIewModel
- Coroutine
- Coil

[DI]
- Hilt

## チーム開発の決まり（どんどん変えていこう）
1. issueを立てる
2. develop/(ブランチ名) でmasterブランチから切る
3. 開発する
4. テンプレのプルリクからプルリクを作成する（多分自動適用されるはず、、？？）
5. 1人以上のレビューをもらう
6. マージ！！
