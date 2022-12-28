# アーキテクチャ

```mermaid
classDiagram

Controller ..> Content
Content ..> Entity
Controller ..> Service
Service ..> Entity
Service ..> Repository
Repository..>Entity
Repository <|-- DatabaseRepository
```

```mermaid
sequenceDiagram

participant Client
participant Controller
participant Content
participant Service

Client->>Controller: Request
Controller->> Content: RequestをContentに変換
Content->>Controller: ContentをEntityに変換
Controller->>Service: Entityを処理依頼
Service->>Repository: 読み書き依頼
Repository->>Service: 読み書き完了
Service->>Controller: 処理結果
Controller->>Content: 処理結果をContentに変換
Content ->> Controller: 変換結果
Controller ->> Client: Response
```