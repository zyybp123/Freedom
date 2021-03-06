package com.bpz.commonlibrary.interf;

/**
 * 文件类型的接口
 */
public interface FileType {
    String EZ = "application/andrew-inset";
    String TSP = "application/dsptype";
    String SPL = "application/futuresplash";
    String HTA = "application/hta";
    String HQX = "application/mac-binhex40";
    String CPT = "application/mac-compactpro";
    String NB = "application/mathematica";
    String MDB = "application/msaccess";
    String ODA = "application/oda";
    String OGG = "application/ogg";
    String PDF = "application/pdf";
    String KEY = "application/pgp-keys";
    String PGP = "application/pgp-signature";
    String PRF = "application/pics-rules";
    String RAR = "application/rar";
    String RDF = "application/rdf+xml";
    String RSS = "application/rss+xml";
    String ZIP = "application/zip";
    String APK = "application/vnd.android.package-archive";
    String CDY = "application/vnd.cinderella";
    String STL = "application/vnd.ms-pki.stl";
    String ODB = "application/vnd.oasis.opendocument.database";
    String ODF = "application/vnd.oasis.opendocument.formula";
    String ODG = "application/vnd.oasis.opendocument.graphics";
    String OTG = "application/vnd.oasis.opendocument.graphics-template";
    String ODI = "application/vnd.oasis.opendocument.image";
    String ODS = "application/vnd.oasis.opendocument.spreadsheet";
    String OTS = "application/vnd.oasis.opendocument.spreadsheet-template";
    String ODT = "application/vnd.oasis.opendocument.text";
    String ODM = "application/vnd.oasis.opendocument.text-master";
    String OTT = "application/vnd.oasis.opendocument.text-template";
    String OTH = "application/vnd.oasis.opendocument.text-web";
    String KML = "application/vnd.google-earth.kml+xml";
    String KMZ = "application/vnd.google-earth.kmz";
    String DOC = "application/msword";//doc,dot
    String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    String DOTX = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
    String XLS = "application/vnd.ms-excel";//xls,xlt
    String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    String XLTX = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
    String PPT = "application/vnd.ms-powerpoint";//ppt,pot,pps
    String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    String POTX = "application/vnd.openxmlformats-officedocument.presentationml.template";
    String PPSX = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
    String COD = "application/vnd.rim.cod";
    String MMF = "application/vnd.smaf";
    String SDC = "application/vnd.stardivision.calc";
    String SDA = "application/vnd.stardivision.draw";
    String SDD = "application/vnd.stardivision.impress";//sdd,sdp
    String SMF = "application/vnd.stardivision.math";
    String SDW = "application/vnd.stardivision.writer";//sdw,vor
    String SGL = "application/vnd.stardivision.writer-global";
    String SXC = "application/vnd.sun.xml.calc";
    String STC = "application/vnd.sun.xml.calc.template";
    String SXD = "application/vnd.sun.xml.draw";
    String STD = "application/vnd.sun.xml.draw.template";
    String SXI = "application/vnd.sun.xml.impress";
    String STI = "application/vnd.sun.xml.impress.template";
    String SXM = "application/vnd.sun.xml.math";
    String SXW = "application/vnd.sun.xml.writer";
    String SXG = "application/vnd.sun.xml.writer.global";
    String STW = "application/vnd.sun.xml.writer.template";
    String VSD = "application/vnd.visio";
    String ABW = "application/x-abiword";
    String DMG = "application/x-apple-diskimage";



    /**
     add("application/x-bcpio", "bcpio");
     add("application/x-bittorrent", "torrent");
     add("application/x-cdf", "cdf");
     add("application/x-cdlink", "vcd");
     add("application/x-chess-pgn", "pgn");
     add("application/x-cpio", "cpio");
     add("application/x-debian-package", "deb");
     add("application/x-debian-package", "udeb");
     add("application/x-director", "dcr");
     add("application/x-director", "dir");
     add("application/x-director", "dxr");
     add("application/x-dms", "dms");
     add("application/x-doom", "wad");
     add("application/x-dvi", "dvi");
     add("application/x-flac", "flac");
     add("application/x-font", "pfa");
     add("application/x-font", "pfb");
     add("application/x-font", "gsf");
     add("application/x-font", "pcf");
     add("application/x-font", "pcf.Z");
     add("application/x-freemind", "mm");
     add("application/x-futuresplash", "spl");
     add("application/x-gnumeric", "gnumeric");
     add("application/x-go-sgf", "sgf");
     add("application/x-graphing-calculator", "gcf");
     add("application/x-gtar", "gtar");
     add("application/x-gtar", "tgz");
     add("application/x-gtar", "taz");
     add("application/x-hdf", "hdf");
     add("application/x-ica", "ica");
     add("application/x-internet-signup", "ins");
     add("application/x-internet-signup", "isp");
     add("application/x-iphone", "iii");
     add("application/x-iso9660-image", "iso");
     add("application/x-jmol", "jmz");
     add("application/x-kchart", "chrt");
     add("application/x-killustrator", "kil");
     add("application/x-koan", "skp");
     add("application/x-koan", "skd");
     add("application/x-koan", "skt");
     add("application/x-koan", "skm");
     add("application/x-kpresenter", "kpr");
     add("application/x-kpresenter", "kpt");
     add("application/x-kspread", "ksp");
     add("application/x-kword", "kwd");
     add("application/x-kword", "kwt");
     add("application/x-latex", "latex");
     add("application/x-lha", "lha");
     add("application/x-lzh", "lzh");
     add("application/x-lzx", "lzx");
     add("application/x-maker", "frm");
     add("application/x-maker", "maker");
     add("application/x-maker", "frame");
     add("application/x-maker", "fb");
     add("application/x-maker", "book");
     add("application/x-maker", "fbdoc");
     add("application/x-mif", "mif");
     add("application/x-ms-wmd", "wmd");
     add("application/x-ms-wmz", "wmz");
     add("application/x-msi", "msi");
     add("application/x-ns-proxy-autoconfig", "pac");
     add("application/x-nwc", "nwc");
     add("application/x-object", "o");
     add("application/x-oz-application", "oza");
     add("application/x-pkcs12", "p12");
     add("application/x-pkcs7-certreqresp", "p7r");
     add("application/x-pkcs7-crl", "crl");
     add("application/x-quicktimeplayer", "qtl");
     add("application/x-shar", "shar");
     add("application/x-shockwave-flash", "swf");
     add("application/x-stuffit", "sit");
     add("application/x-sv4cpio", "sv4cpio");
     add("application/x-sv4crc", "sv4crc");
     add("application/x-tar", "tar");
     add("application/x-texinfo", "texinfo");
     add("application/x-texinfo", "texi");
     add("application/x-troff", "t");
     add("application/x-troff", "roff");
     add("application/x-troff-man", "man");
     add("application/x-ustar", "ustar");
     add("application/x-wais-source", "src");
     add("application/x-wingz", "wz");
     add("application/x-webarchive", "webarchive");
     add("application/x-webarchive-xml", "webarchivexml");
     add("application/x-x509-ca-cert", "crt");
     add("application/x-x509-user-cert", "crt");
     add("application/x-xcf", "xcf");
     add("application/x-xfig", "fig");
     add("application/xhtml+xml", "xhtml");
     add("audio/3gpp", "3gpp");
     add("audio/amr", "amr");
     add("audio/basic", "snd");
     add("audio/midi", "mid");
     add("audio/midi", "midi");
     add("audio/midi", "kar");
     add("audio/midi", "xmf");
     add("audio/mobile-xmf", "mxmf");
     add("audio/mpeg", "mpga");
     add("audio/mpeg", "mpega");
     add("audio/mpeg", "mp2");
     add("audio/mpeg", "mp3");
     add("audio/mpeg", "m4a");
     add("audio/mpegurl", "m3u");
     add("audio/prs.sid", "sid");
     add("audio/x-aiff", "aif");
     add("audio/x-aiff", "aiff");
     add("audio/x-aiff", "aifc");
     add("audio/x-gsm", "gsm");
     add("audio/x-mpegurl", "m3u");
     add("audio/x-ms-wma", "wma");
     add("audio/x-ms-wax", "wax");
     add("audio/x-pn-realaudio", "ra");
     add("audio/x-pn-realaudio", "rm");
     add("audio/x-pn-realaudio", "ram");
     add("audio/x-realaudio", "ra");
     add("audio/x-scpls", "pls");
     add("audio/x-sd2", "sd2");
     add("audio/x-wav", "wav");
     add("image/bmp", "bmp");
     add("audio/x-qcp", "qcp");
     add("image/gif", "gif");
     add("image/ico", "cur");
     add("image/ico", "ico");
     add("image/ief", "ief");
     add("image/jpeg", "jpeg");
     add("image/jpeg", "jpg");
     add("image/jpeg", "jpe");
     add("image/pcx", "pcx");
     add("image/png", "png");
     add("image/svg+xml", "svg");
     add("image/svg+xml", "svgz");
     add("image/tiff", "tiff");
     add("image/tiff", "tif");
     add("image/vnd.djvu", "djvu");
     add("image/vnd.djvu", "djv");
     add("image/vnd.wap.wbmp", "wbmp");
     add("image/x-cmu-raster", "ras");
     add("image/x-coreldraw", "cdr");
     add("image/x-coreldrawpattern", "pat");
     add("image/x-coreldrawtemplate", "cdt");
     add("image/x-corelphotopaint", "cpt");
     add("image/x-icon", "ico");
     add("image/x-jg", "art");
     add("image/x-jng", "jng");
     add("image/x-ms-bmp", "bmp");
     add("image/x-photoshop", "psd");
     add("image/x-portable-anymap", "pnm");
     add("image/x-portable-bitmap", "pbm");
     add("image/x-portable-graymap", "pgm");
     add("image/x-portable-pixmap", "ppm");
     add("image/x-rgb", "rgb");
     add("image/x-xbitmap", "xbm");
     add("image/x-xpixmap", "xpm");
     add("image/x-xwindowdump", "xwd");
     add("model/iges", "igs");
     add("model/iges", "iges");
     add("model/mesh", "msh");
     add("model/mesh", "mesh");
     add("model/mesh", "silo");
     add("text/calendar", "ics");
     add("text/calendar", "icz");
     add("text/comma-separated-values", "csv");
     add("text/css", "css");
     add("text/html", "htm");
     add("text/html", "html");
     add("text/h323", "323");
     add("text/iuls", "uls");
     add("text/mathml", "mml");
     // add ".txt" first so it will be the default for ExtensionFromMimeType
     add("text/plain", "txt");
     add("text/plain", "asc");
     add("text/plain", "text");
     add("text/plain", "diff");
     add("text/plain", "po");     // reserve "pot" for vnd.ms-powerpoint
     add("text/richtext", "rtx");
     add("text/rtf", "rtf");
     add("text/texmacs", "ts");
     add("text/text", "phps");
     add("text/tab-separated-values", "tsv");
     add("text/xml", "xml");
     add("text/x-bibtex", "bib");
     add("text/x-boo", "boo");
     add("text/x-c++hdr", "h++");
     add("text/x-c++hdr", "hpp");
     add("text/x-c++hdr", "hxx");
     add("text/x-c++hdr", "hh");
     add("text/x-c++src", "c++");
     add("text/x-c++src", "cpp");
     add("text/x-c++src", "cxx");
     add("text/x-chdr", "h");
     add("text/x-component", "htc");
     add("text/x-csh", "csh");
     add("text/x-csrc", "c");
     add("text/x-dsrc", "d");
     add("text/x-haskell", "hs");
     add("text/x-java", "java");
     add("text/x-literate-haskell", "lhs");
     add("text/x-moc", "moc");
     add("text/x-pascal", "p");
     add("text/x-pascal", "pas");
     add("text/x-pcs-gcd", "gcd");
     add("text/x-setext", "etx");
     add("text/x-tcl", "tcl");
     add("text/x-tex", "tex");
     add("text/x-tex", "ltx");
     add("text/x-tex", "sty");
     add("text/x-tex", "cls");
     add("text/x-vcalendar", "vcs");
     add("text/x-vcard", "vcf");
     add("video/3gpp", "3gpp");
     add("video/3gpp", "3gp");
     add("video/3gpp", "3g2");
     add("video/dl", "dl");
     add("video/dv", "dif");
     add("video/dv", "dv");
     add("video/fli", "fli");
     add("video/m4v", "m4v");
     add("video/mpeg", "mpeg");
     add("video/mpeg", "mpg");
     add("video/mpeg", "mpe");
     add("video/mp4", "mp4");
     add("video/mpeg", "VOB");
     add("video/quicktime", "qt");
     add("video/quicktime", "mov");
     add("video/vnd.mpegurl", "mxu");
     add("video/webm", "webm");
     add("video/x-la-asf", "lsf");
     add("video/x-la-asf", "lsx");
     add("video/x-mng", "mng");
     add("video/x-ms-asf", "asf");
     add("video/x-ms-asf", "asx");
     add("video/x-ms-wm", "wm");
     add("video/x-ms-wmv", "wmv");
     add("video/x-ms-wmx", "wmx");
     add("video/x-ms-wvx", "wvx");
     add("video/x-msvideo", "avi");
     add("video/x-sgi-movie", "movie");
     add("x-conference/x-cooltalk", "ice");
     add("x-epoc/x-sisx-app", "sisx");
     */
}
