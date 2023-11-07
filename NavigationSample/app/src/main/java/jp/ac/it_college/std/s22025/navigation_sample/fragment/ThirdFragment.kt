package jp.ac.it_college.std.s22025.navigation_sample.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import jp.ac.it_college.std.s22025.navigation_sample.R
import jp.ac.it_college.std.s22025.navigation_sample.databinding.FragmentThirdBinding


class ThirdFragment : Fragment() {
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private val args: ThirdFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //↓secondの方で戻り値がInt型じゃない場合デフォルトの0が返される
//        val choice = arguments?.getInt("choice", 0)
//        binding.display.text = "<$choice>"
        binding.display.text = "<${args.choice}>"
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}