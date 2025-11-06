package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictTransAzure;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.azure.AzureRequest;
import org.sav.fornas.dictminer.model.azure.AzureResponse;
import org.sav.fornas.dictminer.model.azure.AzureTranslation;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictTransAzureRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AzureTranslator extends AbstractTranslator{

	private final RestTemplate azureRestTemplate;
	private final DictTransAzureRepository dictTransAzureRepository;

	@Value("${app-props.url.azure}")
	private String baseURL;
	@Value("${trans_azure_key}")
	private String apiKey;

	@Override
	public WordStates getState() {
		return WordStates.TRANS_AZURE;
	}

	@Override
	public List<String> processWord(DictWord word){
		AzureResponse ar = translate(word.getText());

		List<String> mined = new ArrayList<>();
		for (AzureTranslation at : ar.getTranslations()) {
			DictTransAzure dta = DictTransAzure.builder()
					.dictWordId(word.getId())
					.posTag(at.getPosTag())
					.confidence(at.getConfidence())
					.text(at.getDisplayTarget())
					.build();

			mined.add(dta.getText());
			dictTransAzureRepository.save(dta);
		}
		return mined;
	}

	private AzureResponse translate(String text) {
		AzureRequest ar = new AzureRequest();
		ar.setText(text);
		AzureResponse[] aResp = azureRestTemplate.postForObject("/dictionary/lookup?api-version=3.0&from=en&to=uk", List.of(ar), AzureResponse[].class);
		return aResp != null ? aResp[0] : new AzureResponse();
	}
}
