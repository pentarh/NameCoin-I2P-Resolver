/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.i2p.client.naming.namecoin;

import net.namecoin.NameCoinI2PResolver.NameCoinDNSClient;
import net.i2p.I2PAppContext;
import net.i2p.util.Log;
/**
 *
 * @author pentarh
 */
public final class I2PNMCResolver extends NameCoinDNSClient {
    protected final Log _log;
    protected final I2PAppContext _context;

    @Override
    protected void logger(String severenity, String mess) {
/*        if (severenity.equals("error")) {
            _log.error(mess);
        } else if (severenity.equals("info")) {
            _log.info(mess);
       } else if (severenity.equals("debug")) {
            _log.debug(mess);
       } else if (severenity.equals("warn")) {
            _log.warn(mess);
        }
 *
 */
        if (_log != null) {
            _log.logAlways(Log.WARN, mess);
        }
    }

    public I2PNMCResolver(I2PAppContext context) {
        super(true);
        _appdir=I2PAppContext.getGlobalContext().getAppDir();
        _context = context;
        _log = context.logManager().getLog(getClass());
        _log.debug("I2PNMCResolver Initializing");
        Configuration();
        if (!configurationLoaded) {
            logger("warn","I2PNMCResolver Initialized with errors");
        }
        if (!enabled) {
            logger("warn","I2PNMCResolver Disabling .bit name resolve");
        } else {
            logger("debug", "I2PNMCResolver Initialized success");
        }
    }
}
