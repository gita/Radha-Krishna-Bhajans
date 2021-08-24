package com.hanuman.radha.krishna.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hanuman.radha.krishna.R;


public class MainFragment extends Fragment {

    private ImageView mahamantraBhajan, maiyaMoriBhajan, govindmeroBhajan, ommantraBhajan,
            aartikunjbihariBhajan, badanatkhathaikrishnaBhajan, chotichotigaiyaBhajan, 
            yashomatimaiyaBhajan, meraaapkikripaBhajan, achyutamkeshavamBhajan,
            aredwarpalokanhaiyaBhajan, ek_baar_to_radha_bankarBhajan, gopal_muraliya_waleBhajan,
            govind_bolo_hari_gopal_boloBhajan, kabhi_ram_bankeBhajan, kisi_ne_mera_shyam_dekhaBhajan,
            mithe_ras_se_bharyoBhajan, mohana_muralidharamBhajan,
            om_jai_jagadish_hareBhajan, radhe_radhe_japa_karoBhajan,
            shri_krishna_govind_hare_murariBhajan, shyam_teri_bansi_pukare_radha_naamBhajan,
            shyama_aan_baso_vrindavanBhajan;

    private static final String DRAWABLE = "DRAWABLE";

    public static MainFragment newInstance(int drawableID) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(DRAWABLE, drawableID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mahamantraBhajan = (ImageView) view.findViewById(R.id.mahamantraBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_yashoda_balaram)
                .into(mahamantraBhajan);
        
        maiyaMoriBhajan = (ImageView) view.findViewById(R.id.maiyaMoriBhajan);
        Glide.with(this)
                .load(R.drawable.baby_krishna)
                .into(maiyaMoriBhajan);
        
        govindmeroBhajan = (ImageView) view.findViewById(R.id.govindmeroBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_radha)
                .into(govindmeroBhajan);
        
        ommantraBhajan = (ImageView) view.findViewById(R.id.ommantraBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_5)
                .into(ommantraBhajan);
        
        aartikunjbihariBhajan = (ImageView) view.findViewById(R.id.aartikunjbihariBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_1)
                .into(aartikunjbihariBhajan);
        
        badanatkhathaikrishnaBhajan = (ImageView) view.findViewById(R.id.badanatkhathaikrishnaBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_2)
                .into(badanatkhathaikrishnaBhajan);
        
        chotichotigaiyaBhajan = (ImageView) view.findViewById(R.id.chotichotigaiyaBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_3)
                .into(chotichotigaiyaBhajan);
        
        yashomatimaiyaBhajan = (ImageView) view.findViewById(R.id.yashomatimaiyaBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_vishnu)
                .into(yashomatimaiyaBhajan);
        
        meraaapkikripaBhajan = (ImageView) view.findViewById(R.id.meraaapkikripaBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_brahma)
                .into(meraaapkikripaBhajan);
        
        achyutamkeshavamBhajan = (ImageView) view.findViewById(R.id.achyutamkeshavamBhajan);
        Glide.with(this)
                .load(R.drawable.krishna_kaliya)
                .into(achyutamkeshavamBhajan);

        aredwarpalokanhaiyaBhajan = (ImageView) view.findViewById(R.id.aredwarpalokanhaiyaBhajan);
        Glide.with(this)
                .load(R.drawable.are_dwarpalo)
                .into(aredwarpalokanhaiyaBhajan);

        ek_baar_to_radha_bankarBhajan = (ImageView) view.findViewById(R.id.ek_baar_to_radha_bankarBhajan);
        Glide.with(this)
                .load(R.drawable.ek_baar_to_radha)
                .into(ek_baar_to_radha_bankarBhajan);

        gopal_muraliya_waleBhajan = (ImageView) view.findViewById(R.id.gopal_muraliya_waleBhajan);
        Glide.with(this)
                .load(R.drawable.gopal_muraliya_wale)
                .into(gopal_muraliya_waleBhajan);

        govind_bolo_hari_gopal_boloBhajan = (ImageView) view.findViewById(R.id.govind_bolo_hari_gopal_boloBhajan);
        Glide.with(this)
                .load(R.drawable.govind_bolo_hari_gopal)
                .into(govind_bolo_hari_gopal_boloBhajan);

        kabhi_ram_bankeBhajan = (ImageView) view.findViewById(R.id.kabhi_ram_bankeBhajan);
        Glide.with(this)
                .load(R.drawable.kabhi_ram_banke_kabhi_shyam)
                .into(kabhi_ram_bankeBhajan);

        kisi_ne_mera_shyam_dekhaBhajan = (ImageView) view.findViewById(R.id.kisi_ne_mera_shyam_dekhaBhajan);
        Glide.with(this)
                .load(R.drawable.radha_dhoond_rahi)
                .into(kisi_ne_mera_shyam_dekhaBhajan);

        mithe_ras_se_bharyoBhajan = (ImageView) view.findViewById(R.id.mithe_ras_se_bharyoBhajan);
        Glide.with(this)
                .load(R.drawable.mithe_ras_se_bharyo)
                .into(mithe_ras_se_bharyoBhajan);

        mohana_muralidharamBhajan = (ImageView) view.findViewById(R.id.mohana_muralidharamBhajan);
        Glide.with(this)
                .load(R.drawable.mohana_muralidhara)
                .into(mohana_muralidharamBhajan);

        om_jai_jagadish_hareBhajan = (ImageView) view.findViewById(R.id.om_jai_jagadish_hareBhajan);
        Glide.with(this)
                .load(R.drawable.om_jai_jagdish_hare)
                .into(om_jai_jagadish_hareBhajan);

        radhe_radhe_japa_karoBhajan = (ImageView) view.findViewById(R.id.radhe_radhe_japa_karoBhajan);
        Glide.with(this)
                .load(R.drawable.radhe_radhe_japo)
                .into(radhe_radhe_japa_karoBhajan);

        shri_krishna_govind_hare_murariBhajan = (ImageView) view.findViewById(R.id.shri_krishna_govind_hare_murariBhajan);
        Glide.with(this)
                .load(R.drawable.shri_krishna_goving_hare_murari)
                .into(shri_krishna_govind_hare_murariBhajan);

        shyam_teri_bansi_pukare_radha_naamBhajan = (ImageView) view.findViewById(R.id.shyam_teri_bansi_pukare_radha_naamBhajan);
        Glide.with(this)
                .load(R.drawable.shyam_teri_bansi)
                .into(shyam_teri_bansi_pukare_radha_naamBhajan);

        shyama_aan_baso_vrindavanBhajan = (ImageView) view.findViewById(R.id.shyama_aan_baso_vrindavanBhajan);
        Glide.with(this)
                .load(R.drawable.shyama_aan_baso_vrindavan)
                .into(shyama_aan_baso_vrindavanBhajan);

        
        int godImage = getArguments().getInt(DRAWABLE, 0);
        
        switch (godImage) {

            case 1:
                mahamantraBhajan.setVisibility(View.VISIBLE);
                break;
            case 2:
                maiyaMoriBhajan.setVisibility(View.VISIBLE);
                break;
            case 3:
                govindmeroBhajan.setVisibility(View.VISIBLE);
                break;
            case 4:
                ommantraBhajan.setVisibility(View.VISIBLE);
                break;
            case 5:
                aartikunjbihariBhajan.setVisibility(View.VISIBLE);
                break;
            case 6:
                badanatkhathaikrishnaBhajan.setVisibility(View.VISIBLE);
                break;
            case 7:
                chotichotigaiyaBhajan.setVisibility(View.VISIBLE);
                break;
            case 8:
                yashomatimaiyaBhajan.setVisibility(View.VISIBLE);
                break;
            case 9:
                meraaapkikripaBhajan.setVisibility(View.VISIBLE);
                break;
            case 10:
                achyutamkeshavamBhajan.setVisibility(View.VISIBLE);
                break;
            case 11:
                aredwarpalokanhaiyaBhajan.setVisibility(View.VISIBLE);
                break;
            case 12:
                ek_baar_to_radha_bankarBhajan.setVisibility(View.VISIBLE);
                break;
            case 13:
                gopal_muraliya_waleBhajan.setVisibility(View.VISIBLE);
                break;
            case 14:
                govind_bolo_hari_gopal_boloBhajan.setVisibility(View.VISIBLE);
                break;
            case 15:
                kabhi_ram_bankeBhajan.setVisibility(View.VISIBLE);
                break;
            case 16:
                kisi_ne_mera_shyam_dekhaBhajan.setVisibility(View.VISIBLE);
                break;
            case 17:
                mithe_ras_se_bharyoBhajan.setVisibility(View.VISIBLE);
                break;
            case 18:
                mohana_muralidharamBhajan.setVisibility(View.VISIBLE);
                break;
            case 19:
                om_jai_jagadish_hareBhajan.setVisibility(View.VISIBLE);
                break;
            case 20:
                radhe_radhe_japa_karoBhajan.setVisibility(View.VISIBLE);
                break;
            case 21:
                shri_krishna_govind_hare_murariBhajan.setVisibility(View.VISIBLE);
                break;
            case 22:
                shyam_teri_bansi_pukare_radha_naamBhajan.setVisibility(View.VISIBLE);
                break;
            case 23:
                shyama_aan_baso_vrindavanBhajan.setVisibility(View.VISIBLE);
                break;
            default:
                mahamantraBhajan.setVisibility(View.VISIBLE);
                break;
        }

        return view;
    }
}
