package com.ssyijiu.photogallery.bean;

import java.util.List;

/**
 * Created by ssyijiu on 2017/5/17.
 * Github: ssyijiu
 * E-mail: lxmyijiu@163.com
 */

public class MeiZhi {

    /**
     * error : false
     * results : [{"_id":"591a4a02421aa92c794632c8","createdAt":"2017-05-16T08:38:26.35Z","desc":"5-16","publishedAt":"2017-05-16T12:10:38.580Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg","used":true,"who":"daimajai"},{"_id":"59187082421aa91c8da340d1","createdAt":"2017-05-14T22:58:10.836Z","desc":"5-14","publishedAt":"2017-05-15T12:03:44.165Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1ffla9ostxuj20ku0q2abt.jpg","used":true,"who":"带马甲"},{"_id":"59154ae7421aa90c7a8b2b0d","createdAt":"2017-05-12T13:40:55.505Z","desc":"5-13","publishedAt":"2017-05-12T13:44:54.673Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-12-18380140_455327614813449_854681840315793408_n.jpg","used":true,"who":"daimajia"},{"_id":"5913d09d421aa90c7fefdd8e","createdAt":"2017-05-11T10:46:53.608Z","desc":"5-11","publishedAt":"2017-05-11T12:03:09.581Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-11-18380166_305443499890139_8426655762360565760_n.jpg","used":true,"who":"代码家"},{"_id":"591264ce421aa90c7a8b2aec","createdAt":"2017-05-10T08:54:38.531Z","desc":"5-10","publishedAt":"2017-05-10T11:56:10.18Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-10-18382517_1955528334668679_3605707761767153664_n.jpg","used":true,"who":"带马甲"},{"_id":"59110cff421aa90c83a513ff","createdAt":"2017-05-09T08:27:43.31Z","desc":"5-9","publishedAt":"2017-05-09T12:13:25.467Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-09-18443931_429618670743803_5734501112254300160_n.jpg","used":true,"who":"daimajia"},{"_id":"590fe059421aa90c83a513f2","createdAt":"2017-05-08T11:04:57.969Z","desc":"5-8","publishedAt":"2017-05-08T11:22:01.540Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-08-18252341_289400908178710_9137908350942445568_n.jpg","used":true,"who":"daimajia"},{"_id":"590bce25421aa90c7d49ad3c","createdAt":"2017-05-05T08:58:13.502Z","desc":"5-5","publishedAt":"2017-05-05T11:56:35.629Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg","used":true,"who":"daimajia"},{"_id":"590a791e421aa90c83a513d1","createdAt":"2017-05-04T08:43:10.164Z","desc":"5-4","publishedAt":"2017-05-04T11:43:26.66Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-04-18299181_1306649979420798_1108869403736276992_n.jpg","used":true,"who":"daimajia"},{"_id":"58fb32cb421aa9544b774054","createdAt":"2017-04-22T18:39:07.864Z","desc":"浅川梨奈","publishedAt":"2017-05-03T12:00:31.516Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/61e74233ly1feuogwvg27j20p00zkqe7.jpg","used":true,"who":"小萝莉快过来叔叔帮你检查身体"}]
     */

    public boolean error;
    public List<Results> results;


    public static class Results {
        /**
         * _id : 591a4a02421aa92c794632c8
         * createdAt : 2017-05-16T08:38:26.35Z
         * desc : 5-16
         * publishedAt : 2017-05-16T12:10:38.580Z
         * source : chrome
         * type : 福利
         * url : http://ww1.sinaimg.cn/large/610dc034ly1ffmwnrkv1hj20ku0q1wfu.jpg
         * used : true
         * who : daimajai
         */

        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public boolean used;
        public String who;
    }
}
