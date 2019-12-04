[![](https://jitpack.io/v/sovegetables/recyclerviewadapter.svg)](https://jitpack.io/#sovegetables/recyclerviewadapter)

## CommonAdapter
#### Setup
```gradle
maven { url "https://jitpack.io" }

implementation 'com.github.sovegetables.recyclerviewadapter:adapter:0.1.1'

```
#### Usage
- 单类型
```kotlin
adapter = Adapter()
adapter.setOnItemClickListener { _, item, _ ->
    
}
adapter.items = categories
recyclerview.adapter = adapter

private class Adapter : AbsListAdapter<String>() {

    override fun onBindView(holder: CommonViewHolder, s: String, position: Int) {
        val btn = holder.findViewById<TextView>(R.id.btn_category)
        btn.text = s
    }
    
    override fun getLayoutRes(): Int {
        return R.layout.item_categories
    }
}
```
- 支持多类型
```kotlin
val adapter = InternalAdapter()
val list = arrayListOf<Item>()
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.THREE
    }
})
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.ONE
    }
})
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.TWO
    }
})
adapter.items = list
recyclerview.adapter = adapter

private class InternalAdapter: AbsDelegationAdapter<List<Item>>(){

    interface OnActionListener{
        fun onAction1(item: Item)
        fun onAction2(item: Item)
        fun onAction3(item: Item)
    }

    private var listener : OnActionListener? = null

    fun setOnActionListener(l: OnActionListener?){
        listener = l
    }

    init {
        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.ONE
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_one
            }

        })

        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.TWO
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_two
            }

        })

        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){

            override fun onViewCreated(parent: ViewGroup?, holder: LazyRecyclerViewHolder) {
                super.onViewCreated(parent, holder)

                val listener = View.OnClickListener {
                    when(it.id){
                        R.id.btn_one ->{
                            listener?.onAction1(AdapterDelegate.getItemByTag(holder))
                        }
                        R.id.btn_three ->{
                            listener?.onAction3(AdapterDelegate.getItemByTag(holder))
                        }
                    }
                }
                holder.findViewById<Button>(R.id.btn_one).setOnClickListener(listener)
                holder.findViewById<Button>(R.id.btn_two).setOnClickListener(listener)
            }

            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
                AdapterDelegate.setItemTag(holder, t)
                AdapterDelegate.setPositionTag(holder, position)
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.THREE
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_three
            }

        })
    }

    override fun getItemCount(): Int {
        return items?.size?: 0
    }

}
```

#### Feature
- Single View Type
- Multi View Type
- Easy Usage