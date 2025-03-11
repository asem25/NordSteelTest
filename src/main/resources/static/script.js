const apiUrl = "http://localhost:8080/api/notes";
let noteId = null;

async function loadNotes() {
    try {
        const response = await fetch(apiUrl);
        const notes = await response.json();

        const noteList = document.getElementById("noteList");
        noteList.innerHTML = "";

        notes.forEach(note => {
            let li = document.createElement("li");
            li.textContent = note.title;
            li.onclick = () => selectNote(note);
            noteList.appendChild(li);
        });

        if (notes.length > 0) {
            selectNote(notes[0]);
        } else {
            noteId = null;
            document.getElementById("noteTitle").value = "";
            document.getElementById("noteContent").value = "";
        }
    } catch (error) {
        console.error("Ошибка загрузки заметок:", error);
    }
}

function selectNote(note) {
    noteId = note.id;
    document.getElementById("noteTitle").value = note.title;
    document.getElementById("noteContent").value = note.content;
}

async function saveNote() {
    const title = document.getElementById("noteTitle").value.trim();
    const content = document.getElementById("noteContent").value.trim();

    if (title === "" || content === "") {
        alert("Заполните заголовок и текст перед сохранением!");
        return;
    }

    if (noteId) {
        const updatedNote = { id: noteId, title, content };

        try {
            await fetch(`${apiUrl}/${noteId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updatedNote)
            });

            alert("Заметка обновлена!");
            loadNotes();
        } catch (error) {
            console.error("Ошибка сохранения заметки:", error);
        }
    } else {
        const newNote = { title, content };

        try {
            const response = await fetch(apiUrl, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(newNote)
            });

            const note = await response.json();
            alert("Новая заметка создана!");
            loadNotes();
            selectNote(note);
        } catch (error) {
            console.error("Ошибка создания заметки:", error);
        }
    }
}

async function deleteNote() {
    if (!noteId) {
        alert("Выберите заметку для удаления!");
        return;
    }

    try {
        await fetch(`${apiUrl}/${noteId}`, { method: "DELETE" });
        alert("Заметка удалена!");
        noteId = null;
        document.getElementById("noteTitle").value = "";
        document.getElementById("noteContent").value = "";
        loadNotes();
    } catch (error) {
        console.error("Ошибка удаления заметки:", error);
    }
}
async function createNewNote() {
    const newNote = { title: "Новая заметка", content: "Введите текст..." };

    try {
        console.log("Создаем новую заметку...");
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newNote)
        });

        if (!response.ok) {
            throw new Error(`Ошибка: ${response.status}`);
        }

        const note = await response.json();
        console.log("Создана заметка:", note);

        loadNotes();
        setTimeout(() => selectNote(note), 500);
    } catch (error) {
        console.error("Ошибка создания заметки:", error);
    }
}

// Загружаем заметки при старте
window.onload = loadNotes;
