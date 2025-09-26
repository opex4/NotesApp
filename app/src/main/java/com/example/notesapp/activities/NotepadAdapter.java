package com.example.notesapp.activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.dto.NotepadInfoDTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotepadAdapter extends RecyclerView.Adapter<NotepadAdapter.ViewHolder> {

    private List<NotepadInfoDTO> notepads = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(NotepadInfoDTO notepad);
    }

    public NotepadAdapter(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notepad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotepadInfoDTO notepad = notepads.get(position);
        holder.bind(notepad);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(notepad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notepads.size();
    }

    public void setNotepads(List<NotepadInfoDTO> notepads) {
        this.notepads = notepads;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView notepadName;
        private final TextView notepadDate;
        private final TextView notepadAccessType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notepadName = itemView.findViewById(R.id.notepadName);
            notepadDate = itemView.findViewById(R.id.notepadDate);
            notepadAccessType = itemView.findViewById(R.id.notepadAccessType);
        }


        public void bind(NotepadInfoDTO notepad) {
            // Название блокнота
            notepadName.setText("Название: " + notepad.getNotepadName());

            // accessType
            notepadAccessType.setText("Доступ: " + notepad.getAccessType());

            // Форматирование даты
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String dateText = "Создан: " + sdf.format(notepad.getCreatedAt()) + "\nОбновлен: " + sdf.format(notepad.getUpdatedAt());
            notepadDate.setText(dateText);
        }
    }
}