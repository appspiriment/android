package com.appspiriment.baseclasses

open class NavigateOnClickViewModel : BaseViewModel() {
    override fun onViewClicked(viewId: Int) {
        setNavigate(viewId)
    }
}