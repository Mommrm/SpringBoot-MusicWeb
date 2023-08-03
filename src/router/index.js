import VueRouter from "vue-router";
import Vue from "vue";

import MainPage from "../View/MainPage";
import MyWorkSpace from "../View/MyWorkSpace";
import Message from "../View/Message";

import SearchMusic from "../components/SearchMusic";
import RMusic from "../components/RMusic";
import RankMusic from "../components/RankMusic";

import Register from "../View/RegisterPage"
import Land from "../View/LandPage"

Vue.use(VueRouter)
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({

    routes:[
        {path : '/' , redirect: '/mainpage' },
        {path: '/mainpage' , component : MainPage,
            children:[
                {path: "searchmusic" , component:SearchMusic},
                {path: "rmusic" , component:RMusic},
                {path: "rankmusic" , component:RankMusic}
            ]
        },
        {path : '/myworkspace' , component : MyWorkSpace, 
            children:[
                {path: "land" , component:Land},
                {path: "register" , component:Register}
                
            ]},
        {path : '/message' , component : Message},
    ]
})

export default router