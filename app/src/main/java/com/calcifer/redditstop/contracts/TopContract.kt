package com.calcifer.redditstop.contracts

import com.calcifer.redditstop.models.Post

interface TopContract {

    interface View : BaseContract.View {


    }

    interface Presenter : BaseContract.Presenter<View> {


    }

}
