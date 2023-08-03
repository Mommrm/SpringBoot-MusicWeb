import Vue from 'vue'
import Vuex from 'vuex'


Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentSection: "search",
        songs: [],
        userInfo: {
            account: "",
            password: "",
            token: "",
            userId: "",
            userName: "",
            playlistId: "",
        },
        now_song: {
            songId: "",
            songName: "",
            songSinger: "",
            songUrl: "",
            lyric: [],
        }, //获取当前播放的歌曲
        addedCommentIndex: [], //记录已经增加点赞的评论
        
    },

    getters: {
        account: (state) => {
            return state.account;
        },
        password: (state) => {
            return state.password;
        },
        Token: (state) => {
            return state.token;
        },
        userId: (state) => {
            return state.userId;
        },
        userName: (state) => {
            return state.userName;
        },
        playlistId: (state) => {
            return state.playlistId;
        },
        addedCommentIndex: () => {
            return state.addedCommentIndex;
        },
        now_song: () => {
            return state.now_song;
        },
        songs: () => {
            return state.songs;
        },
        currentSection: () => {
            return state.currentSection;
        }
    },

    mutations: {
        setUserInfo(state,userInfo){
            state.userInfo.account = userInfo.account;
            state.userInfo.password = userInfo.password;
            state.userInfo.token = userInfo.token;
            state.userInfo.userId = userInfo.userId;
            state.userInfo.userName = userInfo.userName;
            state.userInfo.playlistId = userInfo.userId;
        },

        setPlaylistId(state,playlistId){
            state.userInfo.playlistId = playlistId;
        },

        setPlaylistId(state,playlistId){
            state.userInfo.playlistId = playlistId;
        },

        addedCommentIndex(state,commentIndex){
            state.addedCommentIndex.push(commentIndex);
        },

        deleteCommentIndex(state,index){
            state.addedCommentIndex.splice(index,1);
        },
        setToken(state,token){
            state.userInfo.token = token;
        },
        setUserInfo(state,user){
            state.userInfo.userId = user.userId;
            state.userInfo.userName = user.userName;
            state.userInfo.account = user.account;
            state.userInfo.password = user.password;
        },

        //设置当前播放歌曲属性
        setNow_song(state,song){
            state.now_song.songId = song.songId;
            state.now_song.songName = song.songName;
            state.now_song.songSinger = song.songSinger;
            state.now_song.songUrl = song.songUrl;
        },
        //单独设置歌曲歌词
        setlyric(state,lyric){
            state.now_song.lyric = lyric;
        },
        //设置搜索到的歌曲
        setSongs(state,search_songs){
            state.songs = search_songs;
        },
        setCurrentSection(state,currentSection){
            state.currentSection = currentSection;
        }
    }
})

export default store