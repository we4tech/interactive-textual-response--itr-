package net.sf.jml;

import java.io.RandomAccessFile;

import net.sf.jml.util.DigestUtils;
import net.sf.jml.util.StringUtils;
import java.io.*;
import java.io.*;
import net.sf.jml.exception.JmlException;

public final class MsnObject {

    public static final int TYPE_CUSTOM_EMOTICON = 2;
    public static final int TYPE_DISPLAY_PICTURE = 3;
    public static final int TYPE_BACKGROUND = 5;
    public static final int TYPE_WINKS = 8;

    private String creator;
    private int type = TYPE_DISPLAY_PICTURE;
    private String location = "joy.tmp";
    private String friendly = "AAA=";
    private String sha1d;
    private String sha1c;
    private byte msnObj[];

    public final static MsnObject getInstance(String creator, byte picture[]) throws JmlException{
        if (creator == null)throw new JmlException(
                "Creator can't null!");
        if (picture == null)throw new JmlException(
                "Picture can't null!");
        return new MsnObject(creator,picture);
    }

    public final static MsnObject getInstance(String creator, String pictureFileName) throws JmlException {
        byte[] pic;
        try {
            RandomAccessFile msnObjFile = new RandomAccessFile(pictureFileName,
                    "r");
            pic = new byte[(int) msnObjFile.length()];
            msnObjFile.readFully(pic);
            msnObjFile.close();
        } catch (FileNotFoundException ex) {
            throw new JmlException(
                    "File " + pictureFileName + " not found!",ex);
        } catch (IOException ex) {
            throw new JmlException(
                    "File " + pictureFileName + " can't access!",ex);
        }
        return getInstance(creator,pic);
    }

    private MsnObject(String creator, byte msnObj[]) {
        this.creator = creator;
        this.msnObj = msnObj;
        generate();
    }

    private void generate() {
        sha1d = StringUtils.encodeBase64(DigestUtils.sha1(msnObj));
        String tmpSha1c = "Creator" + getCreator() + "Size" + msnObj.length +
                          "Type" + this.getType() + "Location" + getLocation() +
                          "Friendly" +
                          this.getFriendly() + "SHA1D" + sha1d;
        sha1c = StringUtils.encodeBase64(DigestUtils.sha1(tmpSha1c.getBytes()));
    }

    public String getCreator() {
        return creator;
    }


    public void setCreator(String creator) {
        this.creator = creator;
        generate();
    }


    public int getSize() {
        return msnObj.length;
    }


    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
        generate();
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
        generate();
    }


    public String getFriendly() {
        return friendly;
    }


    public void setFriendly(String friendly) {
        if (friendly == null) return;
        try {
            friendly = StringUtils.encodeBase64(friendly.getBytes("UTF-16BE"));
        } catch (UnsupportedEncodingException ex) {
            friendly = StringUtils.encodeBase64(friendly.getBytes());
        }
        this.friendly = friendly;
        generate();
    }

    public String getSha1d() {
        return sha1d;
    }


    public String getSha1c() {
        return sha1c;
    }

    public byte[] getMsnObj() {
        return msnObj;
    }


    public String toString() {
        StringBuffer ret = new StringBuffer("<msnobj Creator=");
        ret.append("\"" + this.getCreator() + "\"");
        ret.append(" Size=");
        ret.append("\"" + this.getSize() + "\"");
        ret.append(" Type=");
        ret.append("\"" + this.getType() + "\"");
        ret.append(" Location=");
        ret.append("\"" + this.getLocation() + "\"");
        ret.append(" Friendly=");
        ret.append("\"" + this.getFriendly() + "\"");
        ret.append(" SHA1D=");
        ret.append("\"" + this.getSha1d() + "\"");
        ret.append(" SHA1C=");
        ret.append("\"" + this.getSha1c() + "\"");
        ret.append("/>");
        return ret.toString();
    }

    public boolean equals(Object object) {
        if (getSha1c() == null) return false;

        if (this == object) {
            return true;
        }

        if ((object != null) && (object instanceof MsnObject)) {
            return getSha1c().equals(((MsnObject) object).getSha1c());
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
    }

}
