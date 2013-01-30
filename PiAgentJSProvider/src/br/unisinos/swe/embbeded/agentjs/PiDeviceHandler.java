package br.unisinos.swe.embbeded.agentjs;

import java.io.IOException;

import org.teleal.cling.binding.LocalServiceBindingException;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.ValidationException;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.Icon;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;

import br.unisinos.swe.embbeded.agentjs.services.AgentService;

public class PiDeviceHandler {

	public LocalDevice getDevice() throws ValidationException,
			LocalServiceBindingException, IOException {

		DeviceIdentity identity = new DeviceIdentity(
				UDN.uniqueSystemIdentifier("Exemplo dispositivo inteligente"));

		DeviceType type = new UDADeviceType("DispositivoInteligente", 1);

		DeviceDetails details = new DeviceDetails("Um Dispositivo inteligente",
				new ManufacturerDetails("Unisinos"), new ModelDetails(
						"SmartDevice100", "Dispositivo inteligente com agentes",
						"v1"));

		System.out.println(getClass().getClassLoader().getResource("icon.png").toExternalForm());
		Icon icon = new Icon("image/png", 48, 48, 8, getClass().getClassLoader().getResource("icon.png"));
		

		LocalService<AgentService> agentService = new AnnotationLocalServiceBinder().read(AgentService.class);

		agentService.setManager(new DefaultServiceManager(agentService, AgentService.class)
	    );

		return new LocalDevice(identity, type, details, icon, agentService);
	}

}
