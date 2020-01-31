package br.ufrgs.inf.pet.dinoapi.service.glossary_item;

import br.ufrgs.inf.pet.dinoapi.entity.GlossaryItem;
import br.ufrgs.inf.pet.dinoapi.model.glossary_item.GlossaryItemModel;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryModel;
import br.ufrgs.inf.pet.dinoapi.repository.GlossaryItemRepository;
import br.ufrgs.inf.pet.dinoapi.service.glossary_version.GlossaryVersionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação de {@link GlossaryItemService}
 *
 * @author joao.silva
 */
@Service
public class GlossaryItemServiceImpl implements GlossaryItemService {
    @Autowired
    GlossaryItemRepository glossaryItemRepository;

    @Autowired
    GlossaryVersionServiceImpl glossaryVersionService;

    @Override
    public ResponseEntity<?> save(GlossaryModel glossaryModel) {
        if (glossaryModel != null) {
            List<GlossaryItemModel> glossaryItemModelList = glossaryModel.getItemList();
            List<GlossaryItemModel> savedItems = new ArrayList<>();

            if (glossaryItemModelList != null) {

                for (GlossaryItemModel glossaryItemModel : glossaryItemModelList) {
                    if(glossaryItemModel.isValid()) {

                        GlossaryItem glossaryItem;

                        // Verifica se há um id
                        if(glossaryItemModel.getId() != null) {
                            // Verifica se o dado já existe em banco
                            glossaryItem = glossaryItemRepository.findOneById(glossaryItemModel.getId());

                            // Se existir, atualiza os dados quando houver modificações
                            if (glossaryItem != null) {
                                // Verifica se houver atualização de dados
                                if (glossaryItem.update(glossaryItemModel)) {
                                    // Se houver, salva as atualizações no banco
                                    this.saveGlossaryItem(glossaryItem, glossaryItemModel, savedItems);
                                }
                            }
                        } else {
                            glossaryItem = glossaryItemRepository.findOneByTitle(glossaryItemModel.getTitle());

                            // Se não existir um registro com o mesmo título salva
                            if (glossaryItem == null) {
                                glossaryItem = new GlossaryItem(glossaryItemModel.getTitle(), glossaryItemModel.getText());
                                this.saveGlossaryItem(glossaryItem, glossaryItemModel, savedItems);
                            }
                        }
                    }
                }

                glossaryModel.setItemList(savedItems);

                Long glossaryVersion;

                // Atualiza a versão do glossário
                if (savedItems.size() > 0) {
                    glossaryVersion = glossaryVersionService.updateGlossaryVersion();
                } else {
                    glossaryVersion = glossaryVersionService.getGlossaryVersionNumber();
                }

                glossaryModel.setVersion(glossaryVersion);

                return new ResponseEntity<>(glossaryModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Item inválido!", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getGlossary() {
        List<GlossaryItem> items = glossaryItemRepository.findAllByExistsTrue();
        GlossaryModel glossary = new GlossaryModel();

        for (GlossaryItem item : items) {
            GlossaryItemModel model = new GlossaryItemModel(item.getId(), item.getTitle(), item.getText(), item.getExists());

            glossary.addItem(model);
        }

        glossary.setVersion(glossaryVersionService.getGlossaryVersionNumber());

        return new ResponseEntity<>(glossary, HttpStatus.OK);
    }

    private void saveGlossaryItem(GlossaryItem glossaryItem, GlossaryItemModel glossaryItemModel, List<GlossaryItemModel> savedItems) {
        glossaryItemRepository.save(glossaryItem);
        glossaryItemModel.setId(glossaryItem.getId());
        savedItems.add(glossaryItemModel);
    }
}
