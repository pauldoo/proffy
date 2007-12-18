/*
    Copyright (C) 2007  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

#include <upnp/upnp.h>
#include <iostream>
#include <string>

namespace {
    const std::string SEARCH_TARGET = "urn:schemas-upnp-org:device:InternetGatewayDevice:1";
    UpnpClient_Handle handle;

    int CallbackFunction(Upnp_EventType eventType, void* event, void* cookie)
    {
        try {
            assert(cookie == NULL);
            switch (eventType) {
                case UPNP_DISCOVERY_ADVERTISEMENT_ALIVE:
                case UPNP_DISCOVERY_SEARCH_RESULT:
                {
                    Upnp_Discovery* discoveryEvent = static_cast<Upnp_Discovery*>(event);
                    std::cout << discoveryEvent->Location << std::endl;
                    std::cout << discoveryEvent->DeviceId << std::endl;
                    std::cout << discoveryEvent->DeviceType << std::endl;
                    std::cout << discoveryEvent->ServiceType << std::endl;

                    char* buffer = NULL;
                    char contentType[LINE_SIZE] = {0};
                    if (UpnpDownloadUrlItem(discoveryEvent->Location, &buffer, contentType) != UPNP_E_SUCCESS) {
                        throw std::string("UpnpDownloadUrlItem() failed");
                    }
                    std::cout << buffer << std::endl;
                    free(buffer);

                    IXML_Document* descriptionDocument = NULL;
                    if (UpnpDownloadXmlDoc(discoveryEvent->Location, &descriptionDocument) != UPNP_E_SUCCESS) {
                        throw std::string("Upnp_DownloadXmlDoc() failed");
                    }
                    if (descriptionDocument) {
                        ixmlDocument_free(descriptionDocument);
                    }

                    break;
                }
                default:
                    std::cout << "Unsupported Upnp_EventType: " << eventType << std::endl;
            }

            return 0;
        } catch (const std::string& error) {
            std::cerr << error << std::endl;
            exit(EXIT_FAILURE);
        }
    }
}

int main(void)
{
    try {
        if (UpnpInit(NULL, 0) != UPNP_E_SUCCESS) {
            throw std::string("UpnpInit() failed");
        }
        if (UpnpRegisterClient(CallbackFunction, NULL, &handle) != UPNP_E_SUCCESS) {
            throw std::string("UpnpRegisterClient() failed");
        }
        if (UpnpSearchAsync(handle, 5, SEARCH_TARGET.c_str(), NULL) != UPNP_E_SUCCESS) {
            throw std::string("UpnpSearchAsync() failed");
        }

    } catch (const std::string& error) {
        std::cerr << error << std::endl;
        UpnpFinish();
        return EXIT_FAILURE;
    }
    UpnpFinish();
    return EXIT_SUCCESS;
}

