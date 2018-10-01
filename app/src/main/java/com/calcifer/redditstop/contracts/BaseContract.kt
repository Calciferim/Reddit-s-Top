package com.calcifer.redditstop.contracts

interface BaseContract {

    interface Presenter<in V : View> {

        fun onAttachView(view: V)

        fun onDetachView()

    }


    interface View {
        fun showToast(text: String)

    }
}