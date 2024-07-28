package telran.bullscows;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class BullsCowsProtocol implements Protocol {
BullsCowsService bullsCows;

public BullsCowsProtocol(BullsCowsService bullscows) {
	this.bullsCows = bullscows;
}

	@Override
	public Response getResponse(Request request) {
		String requestType = request.requestType();
		String requestData = request.requestData();
		Response response = null;
		try {
			response = switch(requestType) {
			case "createNewGame" -> createNewGame(requestData);
			case "getMoveResults" -> getMoveResults(requestData);
			case "isGameOver" -> isGameOver(requestData);
			default -> wrongTypeResponse(requestType);
			};
			
		} catch (Exception e) {
			response = wrongDataResponse(e.getMessage());
		}
		return response;
	}

	private Response isGameOver(String requestData) {
		Boolean isGameOver = bullsCows.isGameOver(Long.parseLong(requestData));
		return new Response(ResponseCode.OK, "" + isGameOver);
	}

	private Response wrongDataResponse(String message) {
		
		return new Response(ResponseCode.WRONG_REQUEST_DATA, message);
	}

	private Response wrongTypeResponse(String requestType) {
		
		return new Response(ResponseCode.WRONG_REQUEST_TYPE, requestType);
	}

	private Response getMoveResults(String requestData) {
		JSONObject jsObject = new JSONObject(requestData);
		long id = jsObject.getLong("id");
		String sequence = jsObject.getString("sequence");
		List<MoveResult> results = bullsCows.getMoveResults(id, new Move(id, sequence));
		
		String resultsString = 
		results.stream().map(mr -> {
			return resultToString(mr);
		}).collect(Collectors.joining(";"));
		
		
		return new Response(ResponseCode.OK, "" + resultsString);
	}

	private String resultToString(MoveResult mr) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sequence", mr.sequence());
		jsonObject.put("bulls", mr.bulls());
		jsonObject.put("cows", mr.cows());

		return jsonObject.toString();
	}

	private Response createNewGame(String requestData) {
		long gameId = bullsCows.createNewGame();
		return new Response(ResponseCode.OK, "" + gameId);
	}

}
