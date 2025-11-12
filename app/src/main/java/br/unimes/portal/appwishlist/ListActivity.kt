package br.unimes.portal.appwishlist

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import br.unimes.portal.appwishlist.databinding.ActivityListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.btnVoltarCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        buscarItensDoUsuario()
    }

    private fun buscarItensDoUsuario() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_LONG).show()
            binding.tvListaItens.text = "Erro: Nenhum usuário logado."
            return
        }

        binding.tvListaItens.text = "Carregando itens..."

        db.collection("usuarios").document(userId).collection("itens")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    binding.tvListaItens.text = "Nenhum item cadastrado ainda."
                } else {
                    val listaFormatada = SpannableStringBuilder()
                    for (document in documents) {
                        val nome = document.getString("nome") ?: "Item sem nome"
                        val preco = document.getDouble("preco")
                        val lugar = document.getString("lugar") ?: ""

                        listaFormatada.bold { append("• $nome\n") }
                        if (lugar.isNotBlank()) {
                            listaFormatada.append("  - Onde: $lugar\n")
                        }
                        if (preco != null) {
                            listaFormatada.append("  - Preço: R$ ${"%.2f".format(preco)}\n")
                        }
                        listaFormatada.append("\n")
                    }
                    binding.tvListaItens.text = listaFormatada
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ListActivity", "Erro ao buscar documentos: ", exception)
                Toast.makeText(this, "Falha ao carregar a lista.", Toast.LENGTH_SHORT).show()
                binding.tvListaItens.text = "Ocorreu um erro ao carregar seus itens."
            }
        binding.btnVoltarCadastro.setOnClickListener {
            finish()
        }
    }
}



