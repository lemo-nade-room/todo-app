# エンドポイントの設計

## GET /

### Request

none

### Response
HomeView(text/html)

- HomeViewにはカテゴリごとにTODOが表示される

##  POST /todo/create
### Request

form

```ts
{
  title: string,
  body: string,
  categoryId: number, 
}
```

### Response

HomeView(text/html)

- 新たにTODOを作成する
- GET / にリダイレクト

## POST /todo/update

### Request

form

```ts
{
  todoId: number,
  title: string,
  body: string,
  state: number,
  categoryId: number, 
}
```

### Response

HomeView(text/html)

- 指定されたIDのTODOの内容を上書きする
- GET / にリダイレクト

## POST /todo/delete

### Request

form

```ts
{
    todoId: number,
}
```

### Response

HomeView(text/html)

- 指定されたIDのTODOを削除する
- GET / にリダイレクト

## POST /category/create

### Request

form

```ts
{
    name: string,
    slug: string,
    color: number,
}
```

### Response

HomeView(text/html)

- 新たにカテゴリを作成する
- GET / にリダイレクト

## POST /category/update

### Request

form

```ts
{
    id: number,
    name: string,
    slug: string,
    color: number,
}
```

### Response

HomeView(text/html)

- 指定されたidのカテゴリの値を変更する
- GET / にリダイレクト

## POST /category/delete

### Request

form

```ts
{
    id: number,
}
```

### Response

HomeView(text/html)

- 指定したidのカテゴリとそれに属するTODO全てを削除する
- GET / にリダイレクト