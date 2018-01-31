package br.com.iandev.midiaindoor.facede;

import br.com.iandev.midiaindoor.dao.ChannelDao;

import br.com.iandev.midiaindoor.model.Channel;
import org.hibernate.Transaction;

public class ChannelFacede extends CrudFacede<Channel> {

    public ChannelFacede() {
        super(new ChannelDao());
    }

    public ChannelFacede(Transaction transaction) {
        super(new ChannelDao(), transaction);
    }
}
