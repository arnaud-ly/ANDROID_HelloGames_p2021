package com.example.webservice_example


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_detail_game.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailGameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_game, container, false)
    }

    var game_id : Int = 0

    fun setGameId(id : Int)
    {
        game_id = id
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The base URL where the WebService is located
        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()
        // Use the client to create a service:
        // an object implementing the interface to the WebService
        val service: WSinterface = retrofit.create(WSinterface::class.java)
        val wsCallback: retrofit2.Callback<GameDetail> = object : retrofit2.Callback<GameDetail> {

            override fun onFailure(call: Call<GameDetail>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<GameDetail>, response: Response<GameDetail>) {
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        Glide.with(requireActivity()).load(responseData.picture).into(detailGameImage)
                        DetailGameDataName.text = responseData.name
                        DetailGameDataType.text = responseData.type
                        DetailGameDataNbPlayers.text = responseData.players.toString()
                        DetailGameDataYear.text = responseData.year.toString()
                        DetailGameDescription.text = responseData.description_en

                        DetailGameButtonKnowMore.setOnClickListener {
                            val homePage = Intent(Intent.ACTION_VIEW, Uri.parse(responseData.url))
                            startActivity(homePage)
                        }

                    }
                }
            }
        }

        service.getGameDetail(game_id).enqueue(wsCallback)
    }
}
