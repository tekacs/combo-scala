# Combo Scala library

Actor-based Scala library for interacting with Combo

## Usage

It's as simple as (in MainReceiver):

```scala
def receive: Receive = {
  case Fact(Topic("request_topic"), json) =>
    sender ! Fact("response_topic", json \ "some_field")
}
```

That's it! Just:
- pattern match on an incoming `Fact(topic: Topic, data: JValue)`
- respond to sender with as many `Fact(topic, data)` objects as you like.
  
  (hint: if you send `Fact(topic, """{"some_json": "value"}""")`, the string is automatically parsed as JSON)

## Config

To ensure that messages come in, list the topics you wish to subscribe to in `combo.topics` in `src/main/resources/application.conf`.

```
combo {
  host = somehost.website.com
  port = 8000
  scheme = http

  topics = [
    request_topic
    other_topic
  ]
}
```
