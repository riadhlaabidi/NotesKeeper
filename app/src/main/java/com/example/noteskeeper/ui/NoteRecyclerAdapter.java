package com.example.noteskeeper.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteskeeper.R;
import com.example.noteskeeper.model.Note;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder> {

    List<? extends Note> mNotesList;

    private final Context mContext;
    private final LayoutInflater layoutInflater;

    public NoteRecyclerAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setNoteList(final List<? extends Note> noteList) {
        if (mNotesList == null) {
            mNotesList = noteList;
            notifyItemRangeInserted(0, noteList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mNotesList.size();
                }

                @Override
                public int getNewListSize() {
                    return noteList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mNotesList.get(oldItemPosition).getId() ==
                            noteList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Note newNote = noteList.get(newItemPosition);
                    Note oldNote = mNotesList.get(oldItemPosition);
                    return newNote.getId() == oldNote.getId()
                            && TextUtils.equals(newNote.getTitle(), oldNote.getTitle())
                            && TextUtils.equals(newNote.getText(), oldNote.getText());
                }
            });
            mNotesList = noteList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = layoutInflater.inflate(R.layout.note_list_item, parent, false);
        return new NoteViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String title = mNotesList.get(position).getTitle();
        String text = mNotesList.get(position).getText();

        holder.noteTitle.setText(title);
        holder.noteText.setText(text);
        holder.id = mNotesList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mNotesList == null ? 0 : mNotesList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        protected final TextView noteTitle;
        protected final TextView noteText;
        protected int id;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteText = itemView.findViewById(R.id.note_text);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, NoteActivity.class);
                intent.putExtra(NoteActivity.KEY_NOTE_ID, id);
                mContext.startActivity(intent);
            });
        }
    }
}
