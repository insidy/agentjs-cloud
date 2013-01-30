package br.unisinos.swe.embbeded.agentjs;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;

public class PiServer implements Runnable {

	private PiDeviceHandler _piDevice;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread serverThread = new Thread(new PiServer());
		serverThread.setDaemon(false);
		serverThread.start();
	}

	@Override
	public void run() {
		try {
			final UpnpService upnpService = new UpnpServiceImpl();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			});
			
			_piDevice = new PiDeviceHandler();

			// Add the bound local device to the registry
			upnpService.getRegistry().addDevice(_piDevice.getDevice());

		} catch (Exception ex) {
			System.err.println("Exception occured: " + ex);
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}

}
