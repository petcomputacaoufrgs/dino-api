package br.ufrgs.inf.pet.dinoapi.websocket.model;

import java.util.List;

public abstract class SynchronizableWSGenericModel<DATA_TYPE> {
        protected List<DATA_TYPE> data;

        public List<DATA_TYPE> getData() {
            return data;
        }

        public void setData(List<DATA_TYPE> data) {
            this.data = data;
        }
}
