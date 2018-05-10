package {

import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.display.Sprite;
import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.FileReference;
import flash.system.ApplicationDomain;
import flash.system.LoaderContext;
import flash.system.Security;
import flash.text.TextField;
import flash.utils.ByteArray;

/**
 * @author suchu
 * @since 2018年5月8日
 */
public class Main extends Sprite {
    [Embed(source="../assets/WebRoomNormal.swf", mimeType="application/octet-stream")]
    private static var WebRoomNormalDLL:Class;

    public static function get getWebRoomNormalDll():ByteArray {
        return new WebRoomNormalDLL();
    }

    [Embed(source="../assets/corenormal.swf", mimeType="application/octet-stream")]
    private static var CoreNormalDLL:Class;

    public static function getCoreNormalDLL():ByteArray {
        return new CoreNormalDLL();
    }

    private var loader:Loader = new Loader();//define loader;
    private var output:TextField;//define log panel
    private var mLoaderInfo:LoaderInfo;
    private var loaderContext:LoaderContext = new LoaderContext();
    private var SwfEncrypt:Class;

    public function Main() {
        var btn:TextField = new TextField();
        btn.y = 0;
        btn.text = "export";
        btn.border = true;
        btn.height = 20;
        btn.addEventListener(MouseEvent.CLICK, onClick);

        output = new TextField();
        output.y = 22;
        output.border = true;
        output.wordWrap = true;
        output.multiline = true;

        output.text = 'log panel init \r\n';
        output.width = 600;
        addChild(btn);
        addChild(output);

        initSwf();
    }

    protected function onClick(e:Event):void {
        output.appendText("onclick\r\n");
        exportFile();
    }


    public function initSwf():void {
        Security.allowDomain("*");
        Security.allowInsecureDomain("*");
        loaderContext.applicationDomain = new ApplicationDomain(ApplicationDomain.currentDomain);
        var bte:ByteArray = getWebRoomNormalDll;
        trace('btn:' + bte.length);
        output.appendText('load WebRoomNormal.swf file success:' + bte.length + "\r\n");
        loader.loadBytes(bte, loaderContext);
        addChild(loader);
        loader.contentLoaderInfo.addEventListener(Event.COMPLETE, onComplete)
    }

    private function exportFile():void {
        var encrypted:ByteArray = getCoreNormalDLL();
        output.appendText('load corenormal.swf file success:' + encrypted.length + "\r\n");
        var instance:Object = this.SwfEncrypt["instance"];
        if (instance && typeof instance.decode === 'function') {
            output.appendText("instance SwfEncrypt success\r\n");
            var decrypted:ByteArray = instance.decode(encrypted);
            output.appendText("decrypt corenormal.swf file success byte length:" + decrypted.length + "\r\n");
            var fr:FileReference = new FileReference();
            fr.save(decrypted, "decrypted_room.swf");
        } else {
            output.appendText("can not instance SwfEncrypt\r\n");
        }

    }

    //complete callback
    protected function onComplete(e:Event):void {
        try {
            var info:LoaderInfo = e.currentTarget as LoaderInfo;
            e.target.removeEventListener(Event.COMPLETE, onComplete);
            mLoaderInfo = info;
            this.SwfEncrypt = GetClass("SwfEncrypt", info);
        } catch (error:Error) {
            output.appendText('error:' + error.getStackTrace() + "\r\n");
            trace(error.getStackTrace());
        }
    }


    public function GetClass(name:String, info:LoaderInfo = null):Class {
        try {
            if (null == info) {
                return ApplicationDomain.currentDomain.getDefinition(name) as Class;
            }
            return info.applicationDomain.getDefinition(name) as Class;
        }
        catch (error:Error) {
            trace("No def Class:" + error.getStackTrace() + "\r\n");
            return null;
        }
        return null;
    }

}
}
