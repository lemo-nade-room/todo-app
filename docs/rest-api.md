# RESTful API設計

## /api/category

### GET

カテゴリ情報一覧を返す

#### Request

none

#### Response

200 OK

JSON

```json
{
    "categories": [
      {
        "id": 1,
        "name": "フロントエンド",
        "slug": "front",
        "color": 1
      }
    ]
}
```

### POST

新規カテゴリを1つ作成する

#### Request

JSON

```json
{
  "category": {
    "name": "バックエンド",
    "slug": "back",
    "color": 2
  }
}
```

#### Response

201 Created

### PUT

1つのカテゴリの内容を置き換える

#### Request

JSON

```json
{
  "category": {
    "id": 2,
    "name": "バックエンド",
    "slug": "back",
    "color": 3
  }
}
```

#### Response

200 OK

## /api/category/{category-id}

### DELETE

指定したカテゴリIDのカテゴリを削除する

#### Request

none

#### Response

200 OK

## /api/{slug}

### GET

指定したslugのカテゴリの情報とTodo一覧を返す

#### Request

none

#### Response

200 OK

JSON

```json
{
  "category": {
    "id": 1,
    "name": "フロントエンド",
    "slug": "front",
    "color": 3
  },
  "states": [
    {
      "state": 0,
      "todos": [
        {
          "id": 2,
          "title": "Hello",
          "body": "world",
          "date": "2022/12/12"
        }
      ]
    }
  ]
}
```

## /api/todos

### POST

新規TODOを作成する

#### Request

JSON

```json
{
  "todo": {
    "categoryId": 2,
    "title": "Hello",
    "body": "world"
  }
}
```

#### Response

201 Created

### PUT

idに対応するTODOの内容を変更する

#### Request

JSON

```json
{
  "todo": {
    "id": 3,
    "categoryId": 2,
    "title": "Hello",
    "body": "world",
    "state": 1
  }
}
```

## /api/todo/{todo-id}

### DELETE

#### Request

none

#### Response

200 OK

