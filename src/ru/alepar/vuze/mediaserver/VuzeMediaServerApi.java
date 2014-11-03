package ru.alepar.vuze.mediaserver;

import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.PluginManager;
import org.gudy.azureus2.plugins.disk.DiskManagerFileInfo;
import org.gudy.azureus2.plugins.ipc.IPCException;
import org.gudy.azureus2.plugins.ipc.IPCInterface;

public class VuzeMediaServerApi implements MediaServerApi {

    private final IPCInterface mediaIpc;

    public VuzeMediaServerApi(PluginManager pluginManager) {
        this.mediaIpc = getMediaIpc(pluginManager);
    }

    @Override
    public FileInfo getFileInfos(DiskManagerFileInfo fileInfo) {
        return new FileInfo(
                getContentURL(fileInfo)
        );
    }

    private String getContentURL(DiskManagerFileInfo info) {
        try {
            return chopOffHostname((String) mediaIpc.invoke("getContentURL", new Object[]{info}));
        } catch (IPCException e) {
            throw new RuntimeException("got exception while invoking azupnpav.getContentURL()", e);
        }
    }

    private static IPCInterface getMediaIpc(PluginManager pluginManager) {
        final PluginInterface mediaInterface = pluginManager.getPluginInterfaceByID("azupnpav", true);
        if (mediaInterface == null) {
            throw new RuntimeException("couldnot find azupnpav plugin - check it is installed and started up");
        }
        return mediaInterface.getIPC();
    }

    private static String chopOffHostname(String url) {
        return url.replaceFirst("http://[^:/]+(.*)", "$1");
    }

}
