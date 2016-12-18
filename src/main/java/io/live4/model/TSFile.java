package io.live4.model;

import org.stjs.javascript.annotation.Namespace;

@Namespace("live4api")
public class TSFile {

    @Override
    public int hashCode() {
        return filename == null ? -42 : filename.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TSFile)) {
            return false;
        }
        TSFile other = (TSFile) obj;
        if (this.filename == null) {
            return other.filename == null;
        } else {
            return this.filename.equals(other.filename);
        }
    }

    public String filename;
    public long filesize;
    public long ctime;
    public long startTime;
    public long videoDuration;
    public int timescale;
    public int mseq;
    
    public long getVideoDuration() {
        return videoDuration;
    }

    public long getVideoDurationMsec() {
        if (timescale != 0) {
            return this.getVideoDuration() * 1000 / timescale;
        } else {
            return 0;
        }
    }

    public double getVideoDurationSec() {
        if (timescale != 0) {
            return (double) this.getVideoDuration() / (double) timescale;
        } else {
            return 0.;
        }
    }

    public int getMseq() {
        return mseq;
    }

    public String getFilename() {
        return filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public long getStartTimeMsec() {
        if (timescale != 0) {
            return startTime * 1000 / timescale;
        }
        return 0;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getCtime() {
        return ctime;
    }

    public int getTimescale() {
        return timescale;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }
}