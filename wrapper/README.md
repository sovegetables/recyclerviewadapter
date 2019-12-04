[![](https://jitpack.io/v/sovegetables/recyclerviewadapter.svg)](https://jitpack.io/#sovegetables/recyclerviewadapter)

## WrapperAdapter

#### Setup
```gradle
maven { url "https://jitpack.io" }
implementation 'com.github.sovegetables.recyclerviewadapter:wrapper:0.1.1'

```
#### Usage
- Add header or footer
```java
wrapperAdapter = WrapperAdapter(originAdapter);
recyclerview.setAdapter(wrapperAdapter)

wrapperAdapter.addHeaderView(header1)
wrapperAdapter.addHeaderView(header2)
wrapperAdapter.addFooterView(footer1)
wrapperAdapter.addFooterView(footer2)
```

- wrapper multi adapter to one big adapter
```java
adapters = ArrayList<RecyclerView.Adapter>()
adapters.add(originAdapter1)
adapters.add(originAdapter2)
wrapperAdapter = WrapperAdapter(adapters)
recyclerview.setAdapter(wrapperAdapter)
```

#### Feature
- Don't change original adapter code
- Add multi header
- Add multi footer
- Can wrapper multi adapter to one big adapter