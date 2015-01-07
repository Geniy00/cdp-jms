package ua.com.taxi.service;

import com.epam.cdp.core.entity.BookingRequest;
import com.epam.cdp.core.entity.BookingRequestEnum;
import com.epam.cdp.core.entity.TsException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ua.com.taxi.entity.Booking;
import ua.com.taxi.entity.ClientDetails;
import ua.com.taxi.util.ConverterUtil;
import ua.com.taxi.util.XstreamSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Geniy00
 */
@Service
public class RouterRestClient {

    private static final Logger LOG = Logger.getLogger(RouterRestClient.class);

    private static final String REST_ACTION_URL =
            "execute?orderId={orderId}&bookingRequestId={bookingRequestId}&action={action}&reason={reason}";
    private static final String REST_GET_CUSTOMER_INFO_URL =
            "getCustomerInfo?orderId={orderId}&bookingRequestId={bookingRequestId}";

    private final XstreamSerializer xstreamSerializer = new XstreamSerializer();

    @Autowired
    private RestTemplate restTemplate;

    @Value("${router.rest.url}")
    private String ROUTER_REST_URL;

    public Booking.Status executeActionRequest(final Booking booking, final BookingRequestEnum.Action action)
            throws TsException {
        return executeActionRequest(booking, action, "");
    }

    public Booking.Status executeActionRequest(final Booking booking, final BookingRequestEnum.Action action,
            final String reason) throws TsException {
        final Map<String, String> mapVariables = createActionRequestParams(booking, action, reason);
        final String url = ROUTER_REST_URL + REST_ACTION_URL;
        final String response = sendRestRequest(url, mapVariables);
        final BookingRequest.Status status = parseResponse(response, BookingRequest.Status.class);
        return ConverterUtil.convertBookingRequestStatusToBookingStatus(status);
    }

    public ClientDetails getClientDetails(final Booking booking) throws TsException {
        final Map<String, String> mapVariables = createClientInfoRequestParams(booking);
        String url = ROUTER_REST_URL + REST_GET_CUSTOMER_INFO_URL;
        final String response = sendRestRequest(url, mapVariables);
        return parseResponse(response, ClientDetails.class);
    }

    private Map<String, String> createActionRequestParams(final Booking booking, final BookingRequestEnum.Action action,
            final String reason) {
        final Map<String, String> mapVariables = new HashMap<>();
        mapVariables.putAll(createClientInfoRequestParams(booking));
        mapVariables.put("action", action.toString());
        mapVariables.put("reason", reason);
        return mapVariables;
    }

    private Map<String, String> createClientInfoRequestParams(final Booking booking) {
        final Map<String, String> mapVariables = new HashMap<>();
        mapVariables.put("orderId", booking.getBookingRequest().getOrderId());
        mapVariables.put("bookingRequestId", booking.getBookingRequest().getBookingRequestId().toString());
        return mapVariables;
    }

    private String sendRestRequest(final String url, final Map<String, String> mapVariables) throws TsException {
        try {
            return restTemplate.getForObject(url, String.class, mapVariables);
        } catch (final HttpClientErrorException ex) {
            final String message = "Can't execute GET request to router module";
            LOG.error(message, ex);
            throw new TsException(ex, TsException.Reason.REST_REQUEST_EXECUTION_FAILURE, message);
        }
    }

    private <T> T parseResponse(final String response, final Class<T> deserializedClass) throws TsException {
        try {
            return xstreamSerializer.deserialize(response, deserializedClass);
        } catch (final Exception ex) {
            LOG.error("Can't parse response>>>\n" + response, ex);
            throw new TsException(TsException.Reason.RESPONSE_PARSING_FAILURE, response);
        }
    }

}
