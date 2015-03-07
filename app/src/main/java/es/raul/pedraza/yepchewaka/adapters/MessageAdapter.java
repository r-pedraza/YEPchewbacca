package es.raul.pedraza.yepchewaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import es.raul.pedraza.yepchewaka.constants.ParseConstants;
import es.raul.pedraza.yepchewaka.R;

/**
 * Created by Victor on 28/02/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mMessages;
    
    public MessageAdapter(Context context, List<ParseObject> messages) {
        super(context, R.layout.message_item,messages);
        mContext=context;
        mMessages=messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }



        ParseObject message = mMessages.get(position);
        String fileType = message.getString(ParseConstants.CLAVE_TIPO_ARCHIVO);

        if(fileType!=null) {
            if (fileType.equals(ParseConstants.TIPO_IMAGEN)) {
                holder.iconImageView.setImageResource(R.drawable.ic_picture);

            } else {
                holder.iconImageView.setImageResource(R.drawable.ic_video);
            }
            holder.nameLabel.setText(message.getString(ParseConstants.CLAVE_NOMBRE_REMITENTE));
        }


        return convertView;
    }


    private  static  class  ViewHolder{

        ImageView iconImageView;
        TextView nameLabel;

    }
}