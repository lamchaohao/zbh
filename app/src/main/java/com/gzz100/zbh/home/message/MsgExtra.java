package com.gzz100.zbh.home.message;

/**
 * Created by Lam on 2018/7/4.
 */

public class MsgExtra {

    public MeetingExtra meeting = new MeetingExtra();

    public int type;

    public static class MeetingExtra{
        public String meetingId;
        public String meetingName;
        public String meetingPlaceName;
        public long meetingStartTime;
        public long meetingEndTime;
        public String mimcTopicId;

        @Override
        public String toString() {
            return "MeetingExtra{" +
                    "meetingId='" + meetingId + '\'' +
                    ", meetingName='" + meetingName + '\'' +
                    ", meetingPlaceName='" + meetingPlaceName + '\'' +
                    ", meetingStartTime=" + meetingStartTime +
                    ", meetingEndTime=" + meetingEndTime +
                    '}';
        }
    }
}
