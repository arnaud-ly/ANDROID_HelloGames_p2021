package com.example.webservice_example


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_ws.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WSFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ws, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val baseURL = "http://androidlessonsapi.herokuapp.com/api/"
        // Use GSON library to create our JSON parserval
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()
        // Use the client to create a service:
        // an object implementing the interface to the WebServiceval
        val service: WSinterface = retrofit.create(WSinterface::class.java)

        // Use it in your activity

        Glide.with(requireActivity()).load("http://www.server.com/pic.png").into(imageView)
        // A List to store or objects

        val wsCallBack: Callback<List<GameList>> = object : Callback<List<GameList>> {
            override fun onFailure(call: Call<List<GameList>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")
            }

            override fun onResponse(call: Call<List<GameList>>, response: Response<List<GameList>>) {
                if (response.code() == 200) {
                    // We got our data !
                    val responseData = response.body()
                    if (responseData != null) {
                        var rand1 = Random.nextInt(responseData.size)
                        var rand2 = Random.nextInt(responseData.size)
                        while (rand2 == rand1)
                            rand2 = Random.nextInt(responseData.size)
                        var rand3 = Random.nextInt(responseData.size)
                        while (rand3 == rand1 || rand3 == rand2)
                            rand3 = Random.nextInt(responseData.size)

                        var rand4 = Random.nextInt(responseData.size)
                        while (rand4 == rand1 || rand4 == rand2 || rand4 == rand3)
                            rand4 = Random.nextInt(responseData.size)

                        Glide.with(requireActivity()).load(responseData[rand1].picture).into(imageView)
                        Glide.with(requireActivity()).load(responseData[rand2].picture).into(imageView2)
                        Glide.with(requireActivity()).load(responseData[rand3].picture).into(imageView3)
                        Glide.with(requireActivity()).load(responseData[rand4].picture).into(imageView4)

                        imageView.setOnClickListener {
                            (activity as MainActivity).GoToFragmentDetail(responseData[rand1].id)
                        }

                        imageView2.setOnClickListener {
                            (activity as MainActivity).GoToFragmentDetail(responseData[rand2].id)
                        }

                        imageView3.setOnClickListener {
                            (activity as MainActivity).GoToFragmentDetail(responseData[rand3].id)
                        }

                        imageView4.setOnClickListener {
                            (activity as MainActivity).GoToFragmentDetail(responseData[rand4].id)
                        }
                    }
                }
            }

        }
        service.getAllToDos().enqueue(wsCallBack)
    }
}
